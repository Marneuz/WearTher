package com.marneux.marneweather.model.weather


data class SingleWeatherDetail(
    val itemType: WeatherItem,
    val value: String,
)

enum class WeatherItem {
    MIN_TEMP, MAX_TEMP, SUNRISE, SUNSET, FEELS_LIKE,
    MAX_UV_INDEX, WIND_DIRECTION, WIND_SPEED
}
