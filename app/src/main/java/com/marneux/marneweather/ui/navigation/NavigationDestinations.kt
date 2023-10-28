package com.marneux.marneweather.ui.navigation

sealed class NavigationDestinations(val route: String) {

    object HomeScreen : NavigationDestinations(route = "home_screen")

    object WeatherDetailScreen :
        NavigationDestinations(route = "weather_detail/{nameOfLocation}/{latitude}/{longitude}") {
        const val NAV_ARG_NAME_LOCATION = "nameLocation"
        const val NAV_ARG_LATITUDE = "latitude"
        const val NAV_ARG_LONGITUDE = "longitude"


        fun buildRoute(
            nameLocation: String,
            latitude: String,
            longitude: String
        ) = "weather_detail/$nameLocation/$latitude/$longitude"
    }
}