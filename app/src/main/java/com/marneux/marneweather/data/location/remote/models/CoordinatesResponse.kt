package com.marneux.marneweather.data.location.remote.models


data class CoordinatesResponse(val features: List<Feature>) {

    data class Feature(val geometry: Geometry) {
        data class Geometry(val coordinates: List<String>)
    }

    data class Coordinates(val longitude: String, val latitude: String)
}

val CoordinatesResponse.coordinates: CoordinatesResponse.Coordinates
    get() {
        val (longitude, latitude) = features.first().geometry.coordinates
        return CoordinatesResponse.Coordinates(longitude, latitude)
    }

