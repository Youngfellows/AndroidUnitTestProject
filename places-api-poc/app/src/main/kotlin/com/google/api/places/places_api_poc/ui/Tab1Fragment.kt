/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.places.places_api_poc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.api.places.places_api_poc.R
import com.google.api.places.places_api_poc.daggger.PlaceDetailsSheetLiveData
import com.google.api.places.places_api_poc.daggger.PlacesLiveData
import com.google.api.places.places_api_poc.misc.PermissionDependentTask
import com.google.api.places.places_api_poc.misc.getMyApplication
import com.google.api.places.places_api_poc.misc.log
import com.google.api.places.places_api_poc.misc.snack
import com.google.api.places.places_api_poc.model.PlaceWrapper
import com.google.api.places.places_api_poc.service.GetCurrentPlaceService
import javax.inject.Inject

class Tab1Fragment : BaseTabFragment() {

    private lateinit var fab: FloatingActionButton
    internal lateinit var recyclerView: RecyclerView
    private lateinit var fragmentContainer: CoordinatorLayout
    @Inject
    lateinit var liveDataGetCurrentPlaces: PlacesLiveData
    @Inject
    lateinit var liveDataPlaceDetailsSheet: PlaceDetailsSheetLiveData
    @Inject
    lateinit var serviceGetCurrentPlace: GetCurrentPlaceService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment.
        val layout = inflater.inflate(R.layout.fragment_tab1, container, false)

        fab = layout.findViewById(R.id.fab_current_place)
        recyclerView = layout.findViewById(R.id.rv_current_place_list)
        fragmentContainer = layout.findViewById(R.id.layout_tab1_root)

        return layout
    }

    override fun onFragmentCreate() {
        // Inject objects into fields.
        getMyApplication().activityComponent?.inject(this)

        // Setup RecyclerView.
        Tab1RecyclerViewHandler(this,
                                liveDataGetCurrentPlaces)

        // Attach a behavior to the FAB.
        fab.setOnClickListener { _ ->
            getParentActivity().executeTaskOnPermissionGranted(
                    object : PermissionDependentTask {
                        override fun getRequiredPermission() =
                                android.Manifest.permission.ACCESS_FINE_LOCATION

                        override fun onPermissionGranted() {
                            serviceGetCurrentPlace.execute()
                            snack(fragmentContainer) {
                                setText(R.string.message_making_api_call_getCurrentPlace)
                            }

                        }

                        @SuppressLint("WrongConstant")
                        override fun onPermissionRevoked() {
                            snack(fragmentContainer) {
                                setText(resources.getString(
                                        R.string.message_permission_missing_for_api_call,
                                        getRequiredPermission()))
                                duration = Snackbar.LENGTH_LONG
                            }
                        }
                    })
        }

    }

}

private class Tab1RecyclerViewHandler(fragment: Tab1Fragment,
                                      getCurrentPlaceLiveData: PlacesLiveData) {

    init {
        // Create the RecyclerView Adapter.
        val dataAdapter = DataAdapter(
                fragment)

        // Attach LiveData observers for current place data (from Places API).
        getCurrentPlaceLiveData.observe(
                fragment,
                Observer { data ->
                    "🎉 observable reacting -> #places=${data.size}".log()
                    dataAdapter.loadData(data)
                })

        // Setup RecyclerView.
        with(fragment.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }


    // List Adapter.
    class DataAdapter(val fragment: Tab1Fragment) : RecyclerView.Adapter<RowViewHolder>() {
        // Underlying data storage.
        val underlyingData: MutableList<PlaceWrapper> = mutableListOf()

        // Load underlying data and update RecyclerView.
        fun loadData(data: List<PlaceWrapper>) {
            underlyingData.apply {
                clear()
                addAll(data)
            }
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return underlyingData.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
            val activity = fragment.getParentActivity()
            val inflatedView = activity.layoutInflater.inflate(
                    R.layout.item_row_place, parent, false)
            return RowViewHolder(
                    fragment,
                    inflatedView)
        }

        override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
            holder.bindToDataItem(underlyingData[position])
        }

    }

    // Row renderer (ViewHolder).
    class RowViewHolder(val fragment: Tab1Fragment, itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        // Get the row renderer from the itemView that's passed (which loads R.layout.item_row_place)
        private val rowView: TextView = itemView.findViewById(R.id.text_row_place)

        fun bindToDataItem(place: PlaceWrapper) {
            rowView.text = place.name
            rowView.setOnClickListener {
                fragment.liveDataPlaceDetailsSheet.setPlace(place)
            }
        }

    }
}

