package com.marneux.marneweather.presentation.common.model
//
//import com.marneux.marneweather.R
//
//class WeatherCodesIcons {
//    //Codigos de clima de la API
//    private val cloudy = setOf(1, 2, 3)
//    private val foggy = setOf(45, 48)
//    private val rainy = setOf(51, 53, 55, 56, 57, 80, 81, 82)
//    private val thunderstorms = setOf(61, 63, 65, 66, 67, 95, 96, 99)
//    private val snowy = setOf(71, 73, 75, 77, 85, 86)
//
//    fun getWeatherImageForCode(weatherCode: Int, isDay: Boolean): Int {
//        if (isDay) {
//            return when (weatherCode) {
//                0 -> R.drawable.img_day_clear
//                in cloudy -> R.drawable.img_day_cloudy
//                in rainy -> R.drawable.img_day_rain
//                in thunderstorms -> R.drawable.img_day_thunder
//                in snowy -> R.drawable.img_day_snow
//                in foggy -> R.drawable.img_day_fog
//                else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
//            }
//        }
//        return when (weatherCode) {
//            0 -> R.drawable.img_night_clear
//            in cloudy -> R.drawable.img_night_cloudy
//            in rainy -> R.drawable.img_night_rain
//            in thunderstorms -> R.drawable.img_night_thunder
//            in snowy -> R.drawable.img_night_snow
//            in foggy -> R.drawable.img_night_fog
//            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
//        }
//    }
//
//    fun getWeatherIconResForCode(
//        weatherCode: Int,
//        isDay: Boolean
//    ): Int {
//        if (isDay) {
//            return when (weatherCode) {
//                0 -> R.drawable.ic_day_clear
//                in cloudy -> R.drawable.ic_day_few_clouds
//                in rainy -> R.drawable.ic_day_rain
//                in thunderstorms -> R.drawable.ic_day_rain
//                in snowy -> R.drawable.ic_day_snow
//                in foggy -> R.drawable.ic_mist
//                else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
//            }
//        }
//        return when (weatherCode) {
//            0 -> R.drawable.ic_night_clear
//            in cloudy -> R.drawable.ic_night_few_clouds
//            in rainy -> R.drawable.ic_night_rain
//            in thunderstorms -> R.drawable.ic_night_rain
//            in snowy -> R.drawable.ic_night_snow
//            in foggy -> R.drawable.ic_mist
//            else -> throw IllegalArgumentException("Unknown weatherCode $weatherCode")
//        }
//    }
//    // Mapa de códigos meteorológicos a descripciones.
//    private val weatherCodeToDescriptionMap = mapOf(
//        0 to "Clear sky",
//        1 to "Mainly clear",
//        2 to "Partly cloudy",
//        3 to "Overcast",
//        45 to "Fog",
//        48 to "Depositing rime fog",
//        51 to "Drizzle",
//        53 to "Drizzle",
//        55 to "Drizzle",
//        56 to "Freezing drizzle",
//        57 to "Freezing drizzle",
//        61 to "Slight rain",
//        63 to "Moderate rain",
//        65 to "Heavy rain",
//        66 to "Light freezing rain",
//        67 to "Heavy freezing rain",
//        71 to "Slight snow fall",
//        73 to "Moderate snow fall",
//        75 to "Heavy snow fall",
//        77 to "Snow grains",
//        80 to "Slight rain showers",
//        81 to "Moderate rain showers",
//        82 to "Violent rain showers",
//        85 to "Slight snow showers",
//        86 to "Heavy snow showers",
//        95 to "Thunderstorms",
//        96 to "Thunderstorms with slight hail",
//        99 to "Thunderstorms with heavy hail",
//    )
//}
