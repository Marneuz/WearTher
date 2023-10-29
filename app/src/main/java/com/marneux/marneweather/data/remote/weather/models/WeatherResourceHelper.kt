package com.marneux.marneweather.data.remote.weather.models

import com.marneux.marneweather.R

//Codigos de clima de la API
private val cloudy = setOf(1, 2, 3)
private val foggy = setOf(45, 48)
private val rainy = setOf(51, 53, 55, 56, 57, 80, 81, 82)
private val thunderstorms = setOf(61, 63, 65, 66, 67, 95, 96, 99)
private val snowy = setOf(71, 73, 75, 77, 85, 86)

fun getWeatherImageForCode(weatherCode: Int, isDay: Boolean): Int {
    if (isDay) {
        return when (weatherCode) {
            0 -> R.drawable.img_day_clear
            in cloudy -> R.drawable.img_day_cloudy
            in rainy -> R.drawable.img_day_rain
            in thunderstorms -> R.drawable.img_day_thunder
            in snowy -> R.drawable.img_day_snow
            in foggy -> R.drawable.img_day_fog
            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
        }
    }
    return when (weatherCode) {
        0 -> R.drawable.img_night_clear
        in cloudy -> R.drawable.img_night_cloudy
        in rainy -> R.drawable.img_night_rain
        in thunderstorms -> R.drawable.img_night_thunder
        in snowy -> R.drawable.img_night_snow
        in foggy -> R.drawable.img_night_fog
        else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
    }
}

fun getWeatherIconResForCode(
    weatherCode: Int,
    isDay: Boolean
): Int {
    if (isDay) {
        return when (weatherCode) {
            0 -> R.drawable.ic_day_clear
            in cloudy -> R.drawable.ic_day_few_clouds
            in rainy -> R.drawable.ic_day_rain
            in thunderstorms -> R.drawable.ic_day_rain
            in snowy -> R.drawable.ic_day_snow
            in foggy -> R.drawable.ic_mist
            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
        }
    }
    return when (weatherCode) {
        0 -> R.drawable.ic_night_clear
        in cloudy -> R.drawable.ic_night_few_clouds
        in rainy -> R.drawable.ic_night_rain
        in thunderstorms -> R.drawable.ic_night_rain
        in snowy -> R.drawable.ic_night_snow
        in foggy -> R.drawable.ic_mist
        else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
    }
}
