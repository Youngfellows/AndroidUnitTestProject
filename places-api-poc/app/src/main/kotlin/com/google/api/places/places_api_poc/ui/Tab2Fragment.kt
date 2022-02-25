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

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.snackbar.Snackbar
import com.google.api.places.places_api_poc.R
import com.google.api.places.places_api_poc.daggger.AutocompletePredictionsLiveData
import com.google.api.places.places_api_poc.daggger.LocationLiveData
import com.google.api.places.places_api_poc.misc.*
import com.google.api.places.places_api_poc.model.AutocompletePredictionData
import com.google.api.places.places_api_poc.service.GetAutocompletePredictionsService
import com.google.api.places.places_api_poc.service.GetLastLocationService
import com.google.api.places.places_api_poc.service.GetPlaceByIDService
import javax.inject.Inject

class Tab2Fragment : BaseTabFragment() {

    private lateinit var textInputQuery: EditText
    internal lateinit var fragmentContainer: CoordinatorLayout
    internal lateinit var recyclerView: RecyclerView
    @Inject
    lateinit var liveDataAutocompletePredictions: AutocompletePredictionsLiveData
    @Inject
    lateinit var liveDataLocation: LocationLiveData
    @Inject
    lateinit var serviceGetPlaceByID: GetPlaceByIDService
    @Inject
    lateinit var serviceAutocompletePredictions: GetAutocompletePredictionsService
    @Inject
    lateinit var serviceGetLastLocation: GetLastLocationService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment.
        val layout = inflater.inflate(R.layout.fragment_tab2, container, false)
        with(layout) {
            fragmentContainer = findViewById(R.id.layout_tab2_root)
            textInputQuery = findViewById(R.id.text_input_query)
            recyclerView = findViewById(R.id.rv_autocomplete_prediction_list)
        }
        return layout
    }

    lateinit var locationHandler: LocationHandler
    private lateinit var textChangeListener: TextChangeListener
    private lateinit var recyclerViewHandler: Tab2RecyclerViewHandler

    override fun onFragmentCreate() {
        // Inject objects into fields.
        getMyApplication().activityComponent?.inject(this)

        locationHandler = LocationHandler(this)
        recyclerViewHandler = Tab2RecyclerViewHandler(this)
        textChangeListener = TextChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        locationHandler.getLocation()
        textInputQuery.addTextChangedListener(textChangeListener)
    }

    override fun onStop() {
        super.onStop()
        textInputQuery.removeTextChangedListener(textChangeListener)
    }

}

class Tab2RecyclerViewHandler(fragment: Tab2Fragment) {

    private val dataAdapter: DataAdapter

    init {
        // Create the RecyclerView Adapter.
        dataAdapter = DataAdapter(fragment)

        // Attach LiveData observers for autocomplete prediction data (from Places API).
        fragment.liveDataAutocompletePredictions.observe(
                fragment,
                Observer { data ->
                    "🎉 observable reacting -> #autocompletePredictions=${data.size}".log()
                    dataAdapter.loadData(data)
                }
        )

        // Setup RecyclerView.
        with(fragment.recyclerView) {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }

    // List Adapter.
    class DataAdapter(val fragment: Tab2Fragment) : RecyclerView.Adapter<RowViewHolder>() {
        // Underlying data storage.
        val underlyingData: MutableList<AutocompletePredictionData> = mutableListOf()

        // Load underlying data and update RecyclerView.
        fun loadData(data: List<AutocompletePredictionData>) {
            underlyingData.apply {
                clear()
                addAll(data)
            }
            notifyDataSetChanged()
        }

        fun clearData() {
            underlyingData.clear()
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
    class RowViewHolder(val fragment: Tab2Fragment, itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        // Get the row renderer from the itemView that's passed (which loads R.layout.item_row_place)
        private val rowView: TextView = itemView.findViewById(R.id.text_row_place)

        fun bindToDataItem(data: AutocompletePredictionData) {
            rowView.text = data.primaryText
            rowView.setOnClickListener {
                data.placeId.let { placeId ->
                    fragment.serviceGetPlaceByID.execute(placeId)
                }
            }
        }
    }

}

class TextChangeListener(val fragment: Tab2Fragment) : TextWatcher {
    override fun afterTextChanged(inputString: Editable) {
        respondToTextChange(inputString)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    private fun respondToTextChange(inputString: Editable) {
        val bounds = fragment.locationHandler.getBounds()
        if (bounds != null) {
            if (inputString.isBlank()) {
                fragment.liveDataAutocompletePredictions.value = mutableListOf()
            } else {
                fragment.serviceAutocompletePredictions.execute(
                        inputString.toString(),
                        bounds)
            }
        } else {
            toast(fragment.getParentActivity()) {
                setText(R.string.message_cant_make_autocomplete_request_if_location_is_null)
                duration = Toast.LENGTH_LONG
            }
        }
    }
}

class LocationHandler(val fragment: Tab2Fragment) {
    init {
        observeLocationLiveData()
    }

    fun observeLocationLiveData() {
        fragment.liveDataLocation.observe(
                fragment,
                Observer { location ->
                    val bounds = LatLngRange.getBounds(
                            location)
                    "📌 Current Location = $location".log()
                    "NorthEast: ${getUrl(bounds.northeast.latitude,
                                         bounds.northeast.longitude)}".log()
                    "SouthWest: ${getUrl(bounds.southwest.latitude,
                                         bounds.southwest.longitude)}".log()
                })
    }

    fun getLocation() {
        fragment.getParentActivity().executeTaskOnPermissionGranted(
                object : PermissionDependentTask {
                    override fun getRequiredPermission() =
                            Manifest.permission.ACCESS_FINE_LOCATION

                    override fun onPermissionGranted() {
                        fragment.serviceGetLastLocation.execute()
                        snack(fragment.fragmentContainer) {
                            setText(R.string.message_making_api_call_GetLastLocation)
                        }
                    }

                    @SuppressLint("WrongConstant")
                    override fun onPermissionRevoked() {
                        snack(fragment.fragmentContainer) {
                            setText(fragment.resources.getString(
                                    R.string.message_permission_missing_for_api_call,
                                    getRequiredPermission()))
                            duration = Snackbar.LENGTH_LONG
                        }
                    }
                })
    }

    fun getBounds(): LatLngBounds? {
        val location = fragment.liveDataLocation.value
        return if (location != null) {
            LatLngRange.getBounds(location)
        } else {
            null
        }
    }

}
