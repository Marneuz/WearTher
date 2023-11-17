package com.marneux.marneweather.data.weather.remote.models

import com.google.gson.annotations.SerializedName


data class HourlyWeatherInfoResponse(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("hourly") val hourlyForecast: HourlyForecast
) {

    data class HourlyForecast(
        @SerializedName("time")
        val timestamps: List<String>,

        @SerializedName("precipitation_probability")
        val precipitationProbability: List<Int> = emptyList(),

        @SerializedName("weathercode")
        val weatherCodes: List<Int> = emptyList(),

        @SerializedName("temperature_2m")
        val temperatureForecasts: List<Float> = emptyList()
    )
}