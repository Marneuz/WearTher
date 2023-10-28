package com.marneux.marneweather.di

import com.marneux.marneweather.data.remote.location.LocationReverseGeocoder
import com.marneux.marneweather.data.remote.location.ReverseGeocoder
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.marneux.marneweather.domain.location.CurrentLocationProvider
import com.marneux.marneweather.domain.location.CurrentLocationProviderImpl
import org.koin.core.module.dsl.bind


val locationServiceModule = module {

    singleOf(::CurrentLocationProvider) { bind<CurrentLocationProviderImpl>() }
    single<ReverseGeocoder> {
        LocationReverseGeocoder(get(), get(named(KOIN_IO_COROUTINE_DISPATCHER_NAME))) }

}