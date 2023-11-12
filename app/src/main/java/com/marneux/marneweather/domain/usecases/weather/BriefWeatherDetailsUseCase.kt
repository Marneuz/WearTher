package com.marneux.marneweather.domain.usecases.weather

import com.marneux.marneweather.data.weather.mapper.toBriefWeatherDetails
import com.marneux.marneweather.model.weather.BriefWeatherDetails
import com.marneux.marneweather.model.weather.CurrentWeather

class BriefWeatherDetailsUseCase {
    operator fun invoke(currentWeather: CurrentWeather): BriefWeatherDetails {
        return currentWeather.toBriefWeatherDetails()
    }
}

