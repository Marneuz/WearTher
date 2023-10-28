package com.marneux.marneweather.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.marneux.marneweather.ui.home.HomeViewModel
import com.marneux.marneweather.ui.weatherdetail.WeatherDetailViewModel


val viewModelsModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::WeatherDetailViewModel)
}
