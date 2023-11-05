package com.marneux.marneweather.di


import com.marneux.marneweather.data.generatedsummary.GenerativeTextRepositoryImpl
import com.marneux.marneweather.data.location.LocationServicesRepositoryImpl
import com.marneux.marneweather.data.weather.WeatherRepositoryImpl
import com.marneux.marneweather.domain.repositories.location.LocationServicesRepository
import com.marneux.marneweather.domain.repositories.textgenerator.GenerativeTextRepository
import com.marneux.marneweather.domain.repositories.weather.WeatherRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoriesModule = module {

    singleOf(::LocationServicesRepositoryImpl) {
        bind<LocationServicesRepository>()
    }
    singleOf(::WeatherRepositoryImpl) {
        bind<WeatherRepository>()
    }

    singleOf(::GenerativeTextRepositoryImpl) {
        bind<GenerativeTextRepository>()
    }
}