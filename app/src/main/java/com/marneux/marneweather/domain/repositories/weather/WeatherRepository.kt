package com.marneux.marneweather.domain.repositories.weather

import com.marneux.marneweather.model.weather.CurrentWeather
import com.marneux.marneweather.model.weather.HourlyForecast
import com.marneux.marneweather.model.weather.RainChances
import com.marneux.marneweather.model.weather.SingleWeatherDetail
import java.time.LocalDate


interface WeatherRepository {

    suspend fun fetchWeatherForLocation(
        nameLocation: String,
        latitude: String,
        longitude: String
    ): Result<CurrentWeather>

    suspend fun rainChances(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate> = LocalDate.now()..LocalDate.now().plusDays(1)
    ): Result<List<RainChances>>

    suspend fun fetchHourlyForecasts(
        latitude: String,
        longitude: String,
        dateRange: ClosedRange<LocalDate>
    ): Result<List<HourlyForecast>>

    suspend fun fetchAdditionalInfoItems(
        latitude: String,
        longitude: String
    ): Result<List<SingleWeatherDetail>>
}
