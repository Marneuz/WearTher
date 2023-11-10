package com.marneux.marneweather.data.weather.remote.models

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("current_weather")
    val currentWeatherData: CurrentWeatherData,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)

data class CurrentWeatherData(
    @SerializedName("temperature")
    val temperature: Double,
    @SerializedName("is_day")
    val isDay: Int,
    @SerializedName("weathercode")
    val weatherCode: Int
)

