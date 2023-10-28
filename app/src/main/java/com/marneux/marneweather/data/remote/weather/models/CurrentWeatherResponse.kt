package com.marneux.marneweather.data.remote.weather.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherResponse(
    @Json(name = "current_weather") val currentWeather: CurrentWeather,
    val latitude: String,
    val longitude: String
) {
    @JsonClass(generateAdapter = true)
    data class CurrentWeather(
        val temperature: Double,
        @Json(name = "is_day") val isDay: Int,
        @Json(name = "weathercode") val weatherCode: Int,
    )
}