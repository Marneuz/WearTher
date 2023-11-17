package com.marneux.marneweather.data.weather.mapper

import com.marneux.marneweather.data.weather.remote.models.DailyForecastOptions
import com.marneux.marneweather.model.weather.SingleWeatherDetail
import com.marneux.marneweather.model.weather.WeatherItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

// Esta función extensión convierte un objeto DailyForecastOptions a una lista de SingleWeatherDetail.
fun DailyForecastOptions.toSingleWeatherDetailList(
    timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh : mm a")
): List<SingleWeatherDetail> = additionalForecastOptions.toSingleWeatherDetailList(
    timezone = timezone,
    timeFormat = timeFormat,
)

// Función auxiliar para convertir opciones de pronóstico adicionales a una lista de detalles
// meteorológicos individuales.
private fun DailyForecastOptions.AdditionalForecastOptions.toSingleWeatherDetailList(
    timezone: String,
    timeFormat: DateTimeFormatter,
): List<SingleWeatherDetail> {
    // Requiere que haya solo un valor para la temperatura mínima del día para procesar.
    // Esto es un chequeo de seguridad para asegurar que los datos son para un único día.
//    He dejado el codigo del require hardcodeado para no arrastrar el
// context en esta capa, ya que al ser un mensaje de error no es algo util a nivel usuario.
    require(minTemperature.size == 1)
    {
        "This mapper method will only consider the first value of each list" +
                "Make sure you request the details for only one day."
    }

    // Calcula la temperatura aparente como el promedio de las temperaturas mínima y máxima aparentes.
    val apparentTemperature =
        (minApparentTemperature.first().roundToInt() + maxApparentTemperature.first()
            .roundToInt()) / 2

    // Convierte los tiempos de salida y puesta del sol a formato de hora local.
    val sunriseTimeString = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(sunrise.first()),
        ZoneId.of(timezone)
    ).toLocalTime().format(timeFormat)

    val sunsetTimeString = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(sunset.first()),
        ZoneId.of(timezone)
    ).format(timeFormat)

    /** Crea una lista de detalles meteorológicos individuales con los valores calculados, las
    imagenes se añaden directamente en [WeatherDetailView] para evitar acceder a resources desde
    esta capa */
    return listOf(
        SingleWeatherDetail(
            itemType = WeatherItem.MIN_TEMP,
            value = "${minTemperature.first().roundToInt()}°",
        ),
        SingleWeatherDetail(
            itemType = WeatherItem.MAX_TEMP,
            value = "${maxTemperature.first().roundToInt()}°",
        ),
        SingleWeatherDetail(
            itemType = WeatherItem.SUNRISE,
            value = sunriseTimeString,
        ),
        SingleWeatherDetail(
            itemType = WeatherItem.SUNSET,
            value = sunsetTimeString,
        ),
        SingleWeatherDetail(
            itemType = WeatherItem.FEELS_LIKE,
            value = "${apparentTemperature}°",
        ),
        SingleWeatherDetail(
            itemType = WeatherItem.MAX_UV_INDEX,
            value = maxUvIndex.first().toString(),
        ),
        SingleWeatherDetail(
            itemType = WeatherItem.WIND_DIRECTION,
            value = "${dominantWindDirection.first()}°",
        ),
        SingleWeatherDetail(
            itemType = WeatherItem.WIND_SPEED,
            value = "${windSpeed.first()} Km/h",
        ),
    )
}