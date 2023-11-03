package com.marneux.marneweather.data.remote.weather.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather,

    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
) {
    data class CurrentWeather(
        @SerializedName("temperature")
        val temperature: Double,

        @SerializedName("is_day")
        val isDay: Int,

        @SerializedName("weathercode")
        val weatherCode: Int
    )
}
