package com.marneux.marneweather.di

import com.marneux.marneweather.ui.views.home.HomeViewModel
import com.marneux.marneweather.ui.views.weatherdetail.WeatherDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module


val viewModelsModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::WeatherDetailViewModel)
}
