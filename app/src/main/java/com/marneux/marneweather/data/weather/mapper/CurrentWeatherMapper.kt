package com.marneux.marneweather.data.weather.mapper

import com.marneux.marneweather.data.weather.remote.models.CurrentWeatherResponse
import com.marneux.marneweather.data.weather.remote.models.HourlyWeatherInfoResponse
import com.marneux.marneweather.model.location.Coordinates
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.CurrentWeather
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.model.weather.RainChances
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt

// Convierte una respuesta de pronóstico horario en una lista de objetos HourlyForecast.
fun HourlyWeatherInfoResponse.toHourlyForecasts(): List<HourlyForecast> {
    val hourlyForecastList = mutableListOf<HourlyForecast>()
    for (i in hourlyForecast.timestamps.indices) {

        // Convierte el tiempo Unix a LocalDateTime.
        val epochSeconds = hourlyForecast.timestamps[i].toLong()
        val correspondingLocalTime = LocalDateTime
            .ofInstant(
                Instant.ofEpochSecond(epochSeconds),
                ZoneId.systemDefault()
            )

        // Crea un objeto HourlyForecast para cada hora con los datos correspondientes.
        val hourlyForecast = HourlyForecast(
            dateTime = correspondingLocalTime,
            temperature = hourlyForecast.temperatureForecasts[i].roundToInt(),
            iconDescriptionCode = hourlyForecast.weatherCodes[i],
        )
        hourlyForecastList.add(hourlyForecast)
    }
    return hourlyForecastList
}

// Convierte una respuesta de pronóstico horario en una lista de objetos PrecipitationProbability.
fun HourlyWeatherInfoResponse.toPrecipitationProbabilities(): List<RainChances> {
    val probabilitiesList = mutableListOf<RainChances>()
    for (i in hourlyForecast.timestamps.indices) {
        val epochSeconds = hourlyForecast.timestamps[i].toLong()
        val correspondingLocalDateTime = LocalDateTime
            .ofInstant(
                Instant.ofEpochSecond(epochSeconds),
                ZoneId.systemDefault()
            )
        val rainChances = RainChances(
            dateTime = correspondingLocalDateTime,
            probabilityPercentage = hourlyForecast.precipitationProbability[i],
            latitude = latitude,
            longitude = longitude
        )
        probabilitiesList.add(rainChances)
    }
    return probabilitiesList
}

// Convierte una respuesta de CurrentWeatherResponse en un objeto CurrentWeather.
fun CurrentWeatherResponse.toCurrentWeather(nameLocation: String) = CurrentWeather(
    temperatureRoundedToInt = currentWeatherData.temperature.roundToInt(),
    nameLocation = nameLocation,
    shortDescriptionCode = currentWeatherData.weatherCode,
    isDay = currentWeatherData.isDay,
    coordinates = Coordinates(
        latitude = latitude,
        longitude = longitude,
    )
)

// Extensión de CurrentWeather para convertirlo en un objeto BriefWeatherDetails.
fun CurrentWeather.toBriefWeatherDetails() = BriefWeatherDetails(
    nameLocation = nameLocation,
    temperatureRoundedToInt = temperatureRoundedToInt,
    shortDescriptionCode = shortDescriptionCode,
    coordinates = coordinates
)
