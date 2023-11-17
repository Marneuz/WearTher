package com.marneux.marneweather.data.weather

//import com.marneux.marneweather.data.weather.mapper.toSavedLocation
import com.marneux.marneweather.data.getBodyOrThrowException
import com.marneux.marneweather.data.safeCall
import com.marneux.marneweather.data.weather.mapper.toCurrentWeather
import com.marneux.marneweather.data.weather.mapper.toHourlyForecasts
import com.marneux.marneweather.data.weather.mapper.toPrecipitationProbabilities
import com.marneux.marneweather.data.weather.mapper.toSingleWeatherDetailList
import com.marneux.marneweather.data.weather.remote.WeatherClient
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import com.marneux.marneweather.model.weather.CurrentWeather
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.model.weather.RainChances
import com.marneux.marneweather.model.weather.SingleWeatherDetail
import java.time.LocalDate


class WeatherRepositoryImpl(
    private val weatherClient: WeatherClient,
) : WeatherRepository {

    override suspend fun fetchWeatherForLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ): Result<CurrentWeather> =
        safeCall {
            val response = weatherClient.getWeatherForCoordinates(latitude, longitude)
            response.getBodyOrThrowException().toCurrentWeather(nameLocation)
        }

    override suspend fun rainChances(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<RainChances>> =
        safeCall {
            val probabilities = weatherClient.getHourlyForecast(
                latitude,
                longitude,
                dateRange.start,
                dateRange.endInclusive
            )
            probabilities.getBodyOrThrowException().toPrecipitationProbabilities()
        }

    override suspend fun fetchHourlyForecasts(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<HourlyForecast>> =
        safeCall {
            val forecasts = weatherClient.getHourlyForecast(
                latitude,
                longitude,
                dateRange.start,
                dateRange.endInclusive
            )
            forecasts.getBodyOrThrowException().toHourlyForecasts()
        }

    override suspend fun fetchAdditionalInfoItems(
        latitude: String,
        longitude: String
    ): Result<List<SingleWeatherDetail>> =
        safeCall {
            val today = LocalDate.now()
            val additionalInfo =
                weatherClient.getAdditionalDailyForecastVariables(latitude, longitude, today, today)
            additionalInfo.getBodyOrThrowException().toSingleWeatherDetailList()
        }
}
