package com.marneux.marneweather.presentation.common.model

import com.marneux.marneweather.R
import java.time.LocalDateTime


//Codigos de clima de la API
private val cloudy = setOf(1, 2, 3)
private val foggy = setOf(45, 48)
private val rainy = setOf(51, 53, 55, 56, 57, 80, 81, 82)
private val thunderstorms = setOf(61, 63, 65, 66, 67, 95, 96, 99)
private val snowy = setOf(71, 73, 75, 77, 85, 86)

fun getWeatherImageForCode(weatherCode: Int): Int {
    val isDay = isDaytime()
    return if (isDay) {
        when (weatherCode) {
            0 -> R.drawable.img_day_clear
            in cloudy -> R.drawable.img_day_cloudy
            in rainy -> R.drawable.img_day_rain
            in thunderstorms -> R.drawable.img_day_thunder
            in snowy -> R.drawable.img_day_snow
            in foggy -> R.drawable.img_day_fog
            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
        }
    } else {
        when (weatherCode) {
            0 -> R.drawable.img_night_clear
            in cloudy -> R.drawable.img_night_cloudy
            in rainy -> R.drawable.img_night_rain
            in thunderstorms -> R.drawable.img_night_thunder
            in snowy -> R.drawable.img_night_snow
            in foggy -> R.drawable.img_night_fog
            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
        }
    }
}

fun getWeatherIconResForCode(weatherCode: Int): Int {
    val isDay = isDaytime()
    return if (isDay) {
        when (weatherCode) {
            0 -> R.drawable.ic_day_clear
            in cloudy -> R.drawable.ic_day_few_clouds
            in rainy -> R.drawable.ic_day_rain
            in thunderstorms -> R.drawable.ic_day_thunderstorms
            in snowy -> R.drawable.ic_day_snow
            in foggy -> R.drawable.ic_mist
            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
        }
    } else {
        when (weatherCode) {
            0 -> R.drawable.ic_night_clear
            in cloudy -> R.drawable.ic_night_few_clouds
            in rainy -> R.drawable.ic_night_rain
            in thunderstorms -> R.drawable.ic_night_thunderstorms
            in snowy -> R.drawable.ic_night_snow
            in foggy -> R.drawable.ic_mist
            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
        }
    }
}

fun isDaytime(): Boolean {
    val currentHour = LocalDateTime.now().hour
    return currentHour in 6..18
}
//fun getDescriptionForWeatherCode(code: Int): String {
//    val stringId = weatherCodeToDescriptionMap[code] ?: R.string.weather_clear_sky
//    return getString(stringId)
//}

// Mapa de códigos meteorológicos a descripciones
val weatherCodeToDescriptionMap = mapOf(
    0 to R.string.weather_clear_sky,
    1 to R.string.weather_mainly_clear,
    2 to R.string.weather_partly_cloudy,
    3 to R.string.weather_overcast,
    45 to R.string.weather_fog,
    48 to R.string.weather_depositing_rime_fog,
    51 to R.string.weather_drizzle,
    53 to R.string.weather_drizzle,
    55 to R.string.weather_drizzle,
    56 to R.string.weather_freezing_drizzle,
    57 to R.string.weather_freezing_drizzle,
    61 to R.string.weather_slight_rain,
    63 to R.string.weather_moderate_rain,
    65 to R.string.weather_heavy_rain,
    66 to R.string.weather_light_freezing_rain,
    67 to R.string.weather_heavy_freezing_rain,
    71 to R.string.weather_slight_snow_fall,
    73 to R.string.weather_moderate_snow_fall,
    75 to R.string.weather_heavy_snow_fall,
    77 to R.string.weather_snow_grains,
    80 to R.string.weather_slight_rain_showers,
    81 to R.string.weather_moderate_rain_showers,
    82 to R.string.weather_violent_rain_showers,
    85 to R.string.weather_slight_snow_showers,
    86 to R.string.weather_heavy_snow_showers,
    95 to R.string.weather_thunderstorms,
    96 to R.string.weather_thunderstorms_with_slight_hail,
    99 to R.string.weather_thunderstorms_with_heavy_hail,
)

