package com.marneux.marneweather.di

import com.marneux.marneweather.domain.usecases.location.FetchSuggestedPlacesForQueryUseCase
import com.marneux.marneweather.domain.usecases.location.GetCurrentLocationUseCase
import com.marneux.marneweather.domain.usecases.location.GetLocationNameForCoordinatesUseCase
import com.marneux.marneweather.domain.usecases.textgenerator.GenerateTextForWeatherDetailsUseCase
import com.marneux.marneweather.domain.usecases.weather.DeleteWeatherLocationFromSavedItemUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchAdditionalWeatherInfoItemsListForCurrentDayUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchHourlyForecastUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchHourlyForecastsForNext24HoursUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchHourlyPrecipitationProbabilitiesUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchPrecipitationProbabilitiesForNext24hoursUseCase
import com.marneux.marneweather.domain.usecases.weather.FetchWeatherForLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.GetBriefWeatherDetailsUseCase
import com.marneux.marneweather.domain.usecases.weather.GetSavedLocationsListStreamUseCase
import com.marneux.marneweather.domain.usecases.weather.PermanentlyDeleteWeatherLocationFromSavedItemsUseCase
import com.marneux.marneweather.domain.usecases.weather.SaveWeatherLocationUseCase
import com.marneux.marneweather.domain.usecases.weather.TryRestoringDeletedWeatherLocationUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { FetchSuggestedPlacesForQueryUseCase(get()) }
    factory { GenerateTextForWeatherDetailsUseCase(get()) }
    factory { DeleteWeatherLocationFromSavedItemUseCase(get()) }
    factory { FetchAdditionalWeatherInfoItemsListForCurrentDayUseCase(get()) }
    factory { FetchHourlyForecastsForNext24HoursUseCase(get()) }
    factory { FetchPrecipitationProbabilitiesForNext24hoursUseCase(get()) }
    factory { FetchWeatherForLocationUseCase(get()) }
    factory { GetSavedLocationsListStreamUseCase(get()) }
    factory { SaveWeatherLocationUseCase(get()) }
    factory { TryRestoringDeletedWeatherLocationUseCase(get()) }
    factory { GetLocationNameForCoordinatesUseCase(get()) }
    factory { GetBriefWeatherDetailsUseCase(get()) }
    factory { GetCurrentLocationUseCase(get()) }
    factory { FetchHourlyForecastUseCase(get()) } //no se usa
    factory { PermanentlyDeleteWeatherLocationFromSavedItemsUseCase(get()) } // no se usa
    factory { FetchHourlyPrecipitationProbabilitiesUseCase(get()) } // tampoco se usa
}

//los uso en ambos viewmodel
//  factory { FetchHourlyForecastsForNext24HoursUseCase(get()) }
// factory { FetchWeatherForLocationUseCase(get()) }
// factory { GetSavedLocationsListStreamUseCase(get()) }