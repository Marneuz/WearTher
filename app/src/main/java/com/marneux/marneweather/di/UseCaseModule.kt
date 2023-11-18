package com.marneux.marneweather.di

import com.marneux.marneweather.domain.usecases.location.CurrentLocationUseCase
import com.marneux.marneweather.domain.usecases.location.DeleteSavedLocationUseCase
import com.marneux.marneweather.domain.usecases.location.ListSavedLocationUseCase
import com.marneux.marneweather.domain.usecases.location.LocationNameCoordsUseCase
import com.marneux.marneweather.domain.usecases.location.RestoreLocationUseCase
import com.marneux.marneweather.domain.usecases.location.SaveLocationUseCase
import com.marneux.marneweather.domain.usecases.location.SuggestedLocationUseCase
import com.marneux.marneweather.domain.usecases.textgenerator.GenerateTextWeatherDetailUseCase
import com.marneux.marneweather.domain.usecases.weather.BriefWeatherDetailsUseCase
import com.marneux.marneweather.domain.usecases.weather.HourlyForecastUseCase
import com.marneux.marneweather.domain.usecases.weather.RainChancesUseCase
import com.marneux.marneweather.domain.usecases.weather.TodayWeatherInfoUseCase
import com.marneux.marneweather.domain.usecases.weather.WeatherByLocationUseCase
import org.koin.dsl.module

val useCaseModule = module {
    //Location use cases
    factory { CurrentLocationUseCase(get()) }
    factory { DeleteSavedLocationUseCase(get()) }
    factory { ListSavedLocationUseCase(get()) }
    factory { LocationNameCoordsUseCase(get()) }
    factory { RestoreLocationUseCase(get()) }
    factory { SaveLocationUseCase(get()) }
    factory { SuggestedLocationUseCase(get()) }
    //Geneated Text use cases
    factory { GenerateTextWeatherDetailUseCase(get()) }
    //Weather use cases
    factory { BriefWeatherDetailsUseCase() }
    factory { HourlyForecastUseCase(get()) }
    factory { RainChancesUseCase(get()) }
    factory { TodayWeatherInfoUseCase(get()) }
    factory { WeatherByLocationUseCase(get()) }
}
