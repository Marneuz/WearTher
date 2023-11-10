package com.marneux.marneweather.data.weather.remote

import com.marneux.marneweather.data.weather.remote.models.AdditionalDailyForecastVariablesResponse
import com.marneux.marneweather.data.weather.remote.models.CurrentWeatherResponse
import com.marneux.marneweather.data.weather.remote.models.HourlyWeatherInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate

interface WeatherClient {

    @GET(WeatherClientConstants.EndPoints.FORECAST)
    suspend fun getWeatherForCoordinates(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("temperature_unit") temperatureUnit: WeatherClientConstants.TemperatureUnits
        = WeatherClientConstants.TemperatureUnits.CELSIUS,
        @Query("windspeed_unit") windSpeedUnit: WeatherClientConstants.WindSpeedUnit
        = WeatherClientConstants.WindSpeedUnit.KILOMETERS_PER_HOUR,
        @Query("precipitation_unit") precipitationUnit: WeatherClientConstants.PrecipitationUnit
        = WeatherClientConstants.PrecipitationUnit.MILLIMETERS,
        @Query("current_weather") shouldIncludeCurrentWeatherInformation: Boolean = true
    ): Response<CurrentWeatherResponse>

    @GET(WeatherClientConstants.EndPoints.FORECAST)
    suspend fun getHourlyForecast(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("start_date") startDate: LocalDate,
        @Query("end_date") endDate: LocalDate,
        @Query("timezone") timezoneConfiguration: WeatherClientConstants.TimeZoneConfiguration = WeatherClientConstants.TimeZoneConfiguration.LOCAL_DEVICE_TIMEZONE,
        @Query("precipitation_unit") precipitationUnit: WeatherClientConstants.PrecipitationUnit = WeatherClientConstants.PrecipitationUnit.MILLIMETERS,
        @Query("timeformat") timeFormat: WeatherClientConstants.TimeFormats = WeatherClientConstants.TimeFormats.UNIX_EPOCH_TIME_IN_SECONDS,
        @Query("hourly") hourlyForecastsToReturn: WeatherClientConstants.HourlyForecastItems = WeatherClientConstants.HourlyForecastItems.ALL
    ): Response<HourlyWeatherInfoResponse>

    @GET(WeatherClientConstants.EndPoints.FORECAST)
    suspend fun getAdditionalDailyForecastVariables(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("start_date") startDate: LocalDate,
        @Query("end_date") endDate: LocalDate,
        @Query("timezone") timezoneConfiguration: WeatherClientConstants.TimeZoneConfiguration = WeatherClientConstants.TimeZoneConfiguration.DEFAULT_FOR_GIVEN_COORDINATES,
        @Query("timeformat") timeFormat: WeatherClientConstants.TimeFormats = WeatherClientConstants.TimeFormats.UNIX_EPOCH_TIME_IN_SECONDS,
        @Query("daily") dailyForecastsToReturn: WeatherClientConstants.DailyForecastItems = WeatherClientConstants.DailyForecastItems.ALL
    ): Response<AdditionalDailyForecastVariablesResponse>


}