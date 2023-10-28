package com.marneux.marneweather.data.remote.location.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoordinatesResponse(
    val features: List<Feature>) {

    @JsonClass(generateAdapter = true)
    data class Feature(val geometry: Geometry) {

        @JsonClass(generateAdapter = true)
        data class Geometry(val coordinates: List<String>)
    }

    data class Coordinates(val longitude: String, val latitude: String)
}

val CoordinatesResponse.coordinates: CoordinatesResponse.Coordinates
    get() {
        val (longitude, latitude) = features.first().geometry.coordinates
        return CoordinatesResponse.Coordinates(longitude, latitude)
    }