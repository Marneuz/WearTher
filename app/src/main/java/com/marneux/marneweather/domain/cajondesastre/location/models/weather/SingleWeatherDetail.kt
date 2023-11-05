package com.marneux.marneweather.domain.cajondesastre.location.models.weather

import androidx.annotation.DrawableRes
import com.marneux.marneweather.R
import com.marneux.marneweather.data.weather.remote.models.AdditionalDailyForecastVariablesResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

data class SingleWeatherDetail(
    val name: String,
    val value: String,
    @DrawableRes val iconResId: Int
)

fun AdditionalDailyForecastVariablesResponse.toSingleWeatherDetailList(
    timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh : mm a")
): List<SingleWeatherDetail> = additionalForecastedVariables.toSingleWeatherDetailList(
    timezone = timezone,
    timeFormat = timeFormat
)

private fun AdditionalDailyForecastVariablesResponse.AdditionalForecastedVariables
    .toSingleWeatherDetailList(
    timezone: String,
    timeFormat: DateTimeFormatter
): List<SingleWeatherDetail>{
    require(minTemperatureForTheDay.size == 1) {
        "This mapper method will only consider the first value of each list" +
                "Make sure you request the details for only one day."
    }
    val apparentTemperature =
        (minApparentTemperature.first().roundToInt() + maxApparentTemperature.first()
            .roundToInt()) / 2
    val sunriseTimeString = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(sunrise.first()),
        ZoneId.of(timezone)
    ).toLocalTime().format(timeFormat)
    val sunsetTimeString = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(sunset.first()),
        ZoneId.of(timezone)
    ).format(timeFormat)
    return listOf(
        SingleWeatherDetail(
            name = "Min Temp",
            value = "${minTemperatureForTheDay.first().roundToInt()}°",
            iconResId = R.drawable.ic_thermometer
        ),
        SingleWeatherDetail(
            name = "Max Temp",
            value = "${maxTemperatureForTheDay.first().roundToInt()}°",
            iconResId = R.drawable.ic_thermometer
        ),
        SingleWeatherDetail(
            name = "Sunrise",
            value = sunriseTimeString,
            iconResId = R.drawable.ic_sunrise
        ),
        SingleWeatherDetail(
            name = "Sunset",
            value = sunriseTimeString,
            iconResId = R.drawable.ic_sunset
        ),
        SingleWeatherDetail(
            name = "Feels Like",
            value = "${apparentTemperature}°",
            iconResId = R.drawable.ic_thermometer
        ),
        SingleWeatherDetail(
            name = "Max UV Index",
            value = maxUvIndex.first().toString(),
            iconResId = R.drawable.ic_uv_index
        ),
        SingleWeatherDetail(
            name = "Wind Direction",
            value = "${dominantWindDirection.first()}°",
            iconResId = R.drawable.ic_wind_direction
        ),
        SingleWeatherDetail(
            name = "Wind Speed",
            value = "${windSpeed.first()} Km/h",
            iconResId = R.drawable.ic_wind
        ),

    )
}