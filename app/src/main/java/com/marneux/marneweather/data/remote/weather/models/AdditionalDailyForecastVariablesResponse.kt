package com.marneux.marneweather.data.remote.weather.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

import com.google.gson.annotations.SerializedName

data class AdditionalDailyForecastVariablesResponse(
    @SerializedName("timezone")
    val timezone: String,

    @SerializedName("daily")
    val additionalForecastedVariables: AdditionalForecastedVariables
) {
    data class AdditionalForecastedVariables(
        @SerializedName("temperature_2m_min")
        val minTemperatureForTheDay: List<Double>,

        @SerializedName("temperature_2m_max")
        val maxTemperatureForTheDay: List<Double>,

        @SerializedName("apparent_temperature_max")
        val maxApparentTemperature: List<Double>,

        @SerializedName("apparent_temperature_min")
        val minApparentTemperature: List<Double>,

        @SerializedName("sunrise")
        val sunrise: List<Long>,

        @SerializedName("sunset")
        val sunset: List<Long>,

        @SerializedName("uv_index_max")
        val maxUvIndex: List<Double>,

        @SerializedName("winddirection_10m_dominant")
        val dominantWindDirection: List<Int>,

        @SerializedName("windspeed_10m_max")
        val windSpeed: List<Double>
    )
}
