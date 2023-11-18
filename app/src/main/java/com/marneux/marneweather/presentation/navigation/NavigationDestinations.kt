package com.marneux.marneweather.presentation.navigation

sealed class NavigationDestinations(val route: String) {

    /** Esta es una clase sellada que representa los destinos de navegación en la aplicación.
    Cada objeto representa una pantalla diferente.*/
    // Objeto representando la pantalla de inicio.
    object HomeView : NavigationDestinations(route = "home_view")

    // Objeto representando la pantalla de detalle del clima.
    object WeatherDetailView :
        NavigationDestinations(route = "weather_detail/{nameLocation}/{latitude}/{longitude}") {

        // Constantes para los argumentos de navegación.
        // Se utilizan para pasar y recibir datos entre pantallas.
        const val NAV_ARG_NAME_OF_LOCATION = "nameLocation"
        const val NAV_ARG_LATITUDE = "latitude"
        const val NAV_ARG_LONGITUDE = "longitude"

        // Función para construir la ruta de navegación con los argumentos específicos.
        // Esto se utiliza para navegar a la pantalla de detalles del clima con los datos de ubicación.
        fun buildRoute(
            nameLocation: String,
            latitude: String,
            longitude: String
        ) = "weather_detail/$nameLocation/$latitude/$longitude"
    }
}