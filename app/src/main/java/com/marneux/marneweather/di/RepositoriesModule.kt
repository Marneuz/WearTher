package com.marneux.marneweather.di


import com.marneux.marneweather.data.repositories.location.LocationServicesRepository
import com.marneux.marneweather.data.repositories.location.LocationServicesRepositoryImpl
import com.marneux.marneweather.data.repositories.textgenerator.GenerativeTextRepository
import com.marneux.marneweather.data.repositories.textgenerator.GenerativeTextRepositoryImpl
import com.marneux.marneweather.data.repositories.weather.WeatherRepository
import com.marneux.marneweather.data.repositories.weather.WeatherRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoriesModule = module {

    singleOf(::LocationServicesRepository){
        bind<LocationServicesRepositoryImpl>()
    }
    singleOf(::WeatherRepository){
        bind<WeatherRepositoryImpl>()
    }

    singleOf(::GenerativeTextRepository){
        bind<GenerativeTextRepositoryImpl>()
    }
}