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

package com.google.api.places.places_api_poc.service

import androidx.annotation.WorkerThread
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceBufferResponse
import com.google.api.places.places_api_poc.daggger.PlaceDetailsSheetLiveData
import com.google.api.places.places_api_poc.misc.ExecutorWrapper
import com.google.api.places.places_api_poc.misc.log
import com.google.api.places.places_api_poc.model.PlaceWrapper

class GetPlaceByIDService
constructor(private val executorWrapper: ExecutorWrapper,
            private val client: GeoDataClient,
            private val data: PlaceDetailsSheetLiveData) {

    fun execute(placeId: String) {
        "PlacesAPI ⇢ GeoDataClient.getPlaceById() ✅".log()
        client.getPlaceById(placeId)
                .handleResponse(executorWrapper.executor) { response ->
                    when (response) {
                        is ServiceResponse.Success -> {
                            processPlace(response.value)
                            response.value.release()
                        }
                        is ServiceResponse.Error -> {
                            "⚠️ Task failed with exception ${response.exception}".log()
                        }
                    }
                }
    }

    @WorkerThread
    private fun processPlace(placeBufferResponse: PlaceBufferResponse) {
        val place = placeBufferResponse.get(0)
        data.postPlace(PlaceWrapper(place))
    }

}