package com.marneux.marneweather.data.remote.weather.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AdditionalDailyForecastVariablesResponse(
    @Json(name = "timezone") val timezone: String,
    @Json(name = "daily") val additionalForecastedVariables: AdditionalForecastedVariables
) {
    @JsonClass(generateAdapter = true)
    data class AdditionalForecastedVariables(
        @Json(name = "temperature_2m_min") val minTemperatureForTheDay: List<Double>,
        @Json(name = "temperature_2m_max") val maxTemperatureForTheDay: List<Double>,
        @Json(name = "apparent_temperature_max") val maxApparentTemperature: List<Double>,
        @Json(name = "apparent_temperature_min") val minApparentTemperature: List<Double>,
        @Json(name = "sunrise") val sunrise: List<Long>,
        @Json(name = "sunset") val sunset: List<Long>,
        @Json(name = "uv_index_max") val maxUvIndex: List<Double>,
        @Json(name = "winddirection_10m_dominant") val dominantWindDirection: List<Int>,
        @Json(name = "windspeed_10m_max") val windSpeed: List<Double>
    )
}