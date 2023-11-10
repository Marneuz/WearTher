package com.marneux.marneweather.data.weather.remote

import java.time.ZoneId

object WeatherClientConstants {
    const val BASE_URL = "https://api.open-meteo.com/v1/"

    object EndPoints {
        const val FORECAST = "forecast"
    }

    enum class TemperatureUnits(private val valueToBeSentToTheApi: String) {
        CELSIUS("celsius"),
        FAHRENHEIT("fahrenheit");

        override fun toString(): String = valueToBeSentToTheApi
    }

    enum class WindSpeedUnit(private val valueToBeSentToTheApi: String) {
        KILOMETERS_PER_HOUR("kmh"),
        MILES_PER_HOUR("mph");

        override fun toString(): String = valueToBeSentToTheApi
    }

    enum class PrecipitationUnit(private val valueToBeSentToTheApi: String) {
        MILLIMETERS("mm"),
        INCHES("inch");

        override fun toString(): String = valueToBeSentToTheApi
    }

    enum class DailyForecastItems(private val valueToBeSentToTheApi: String) {
        MAX_TEMPERATURE("temperature_2m_max"),
        MIN_TEMPERATURE("temperature_2m_min"),
        MAX_APPARENT_TEMPERATURE("apparent_temperature_max"),
        MIN_APPARENT_TEMPERATURE("apparent_temperature_min"),
        SUNRISE("sunrise"),
        SUNSET("sunset"),
        UV_INDEX("uv_index_max"),
        WIND_SPEED("windspeed_10m_max"),
        WIND_DIRECTION("winddirection_10m_dominant"),
        ALL(
            "$MAX_TEMPERATURE,$MIN_TEMPERATURE," +
                    "$MAX_APPARENT_TEMPERATURE,$MIN_APPARENT_TEMPERATURE," +
                    "$SUNRISE,$SUNSET,$UV_INDEX,$WIND_SPEED,$WIND_DIRECTION"
        );

        override fun toString(): String = valueToBeSentToTheApi
    }

    enum class HourlyForecastItems(private val valueToBeSentToTheApi: String) {
        PRECIPITATION_PROBABILITIES("precipitation_probability"),
        WEATHER_CODE("weathercode"),
        TEMPERATURE("temperature_2m"),
        ALL("$WEATHER_CODE,$PRECIPITATION_PROBABILITIES,$TEMPERATURE");

        override fun toString(): String = valueToBeSentToTheApi
    }

    enum class TimeFormats(private val valueToBeSentToTheApi: String) {
        UNIX_EPOCH_TIME_IN_SECONDS("unixtime"),
        ISO_8601("iso8601");

        override fun toString(): String = valueToBeSentToTheApi
    }

    enum class TimeZoneConfiguration(private val valueToBeSentToTheApi: String) {
        DEFAULT_FOR_GIVEN_COORDINATES("auto"),
        LOCAL_DEVICE_TIMEZONE(ZoneId.systemDefault().toString());

        override fun toString(): String = valueToBeSentToTheApi
    }
}