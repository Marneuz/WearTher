package com.marneux.marneweather.di

import com.marneux.marneweather.domain.usecases.location.CurrentLocationUseCase
import com.marneux.marneweather.domain.usecases.location.LocationNameFromCoordsUseCase
import com.marneux.marneweather.domain.usecases.location.SuggestedPlacesUseCase
import com.marneux.marneweather.domain.usecases.textgenerator.WeatherDetailsTextUseCase
import com.marneux.marneweather.domain.usecases.weather.BriefWeatherDetailsUseCase
import com.marneux.marneweather.domain.usecases.weather.DeleteSavedLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.HourlyForecastUseCase
import com.marneux.marneweather.domain.usecases.weather.PrecipitationProbabilityUseCase
import com.marneux.marneweather.domain.usecases.weather.RestoreLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.SaveLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.StreamSavedLocationsUseCase
import com.marneux.marneweather.domain.usecases.weather.TodayWeatherInfoUseCase
import com.marneux.marneweather.domain.usecases.weather.WeatherByLocationUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { SuggestedPlacesUseCase(get()) }
    factory { WeatherDetailsTextUseCase(get()) }
    factory { DeleteSavedLocationUseCase(get()) }
    factory { TodayWeatherInfoUseCase(get()) }
    factory { HourlyForecastUseCase(get()) }
    factory { PrecipitationProbabilityUseCase(get()) }
    factory { WeatherByLocationUseCase(get()) }
    factory { StreamSavedLocationsUseCase(get()) }
    factory { SaveLocationUseCase(get()) }
    factory { RestoreLocationUseCase(get()) }
    factory { LocationNameFromCoordsUseCase(get()) }
    factory { BriefWeatherDetailsUseCase() }
    factory { CurrentLocationUseCase(get()) }
}
