package com.marneux.marneweather.domain.cajondesastre.location.models.weather

import com.marneux.marneweather.data.weather.remote.models.HourlyWeatherInfoResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class PrecipitationProbability(
    val latitude: String,
    val longitude: String,
    val dateTime: LocalDateTime,
    val probabilityPercentage: Int
)


fun HourlyWeatherInfoResponse.toPrecipitationProbabilities(): List<PrecipitationProbability> {
    val probabilitiesList = mutableListOf<PrecipitationProbability>()
    for (i in hourlyForecast.timestamps.indices) {
        val epochSeconds = hourlyForecast.timestamps[i].toLong()
        val correspondingLocalDateTime = LocalDateTime
            .ofInstant(
                Instant.ofEpochSecond(epochSeconds),
                ZoneId.systemDefault()
            )

        val precipitationProbability = PrecipitationProbability(
            dateTime = correspondingLocalDateTime,
            probabilityPercentage = hourlyForecast.precipitationProbabilityPercentages[i],
            latitude = latitude,
            longitude = longitude
        )
        probabilitiesList.add(precipitationProbability)
    }
    return probabilitiesList
}
