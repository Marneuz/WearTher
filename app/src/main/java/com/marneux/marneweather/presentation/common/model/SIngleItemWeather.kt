package com.marneux.marneweather.presentation.common.model

import com.marneux.marneweather.R
import com.marneux.marneweather.model.weather.WeatherItem


val weatherItemToStringResMap = mapOf(
    WeatherItem.MIN_TEMP to R.string.min_temp,
    WeatherItem.MAX_TEMP to R.string.max_temp,
    WeatherItem.SUNRISE to R.string.sunrise,
    WeatherItem.SUNSET to R.string.sunset,
    WeatherItem.FEELS_LIKE to R.string.feels_like,
    WeatherItem.MAX_UV_INDEX to R.string.max_uv_index,
    WeatherItem.WIND_DIRECTION to R.string.wind_direction,
    WeatherItem.WIND_SPEED to R.string.wind_speed,
)
val weatherItemToIconResMap = mapOf(
    WeatherItem.MIN_TEMP to R.drawable.ic_min_temp,
    WeatherItem.MAX_TEMP to R.drawable.ic_max_temp,
    WeatherItem.SUNRISE to R.drawable.ic_sunrise,
    WeatherItem.SUNSET to R.drawable.ic_sunset,
    WeatherItem.FEELS_LIKE to R.drawable.ic_wind_pressure,
    WeatherItem.MAX_UV_INDEX to R.drawable.ic_uv_index,
    WeatherItem.WIND_DIRECTION to R.drawable.ic_wind_direction,
    WeatherItem.WIND_SPEED to R.drawable.ic_wind,
)
