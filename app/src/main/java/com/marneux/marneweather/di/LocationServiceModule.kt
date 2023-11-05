package com.marneux.marneweather.di

import com.marneux.marneweather.data.location.remote.LocationReverseGeocoder
import com.marneux.marneweather.data.location.remote.ReverseGeocoder
import com.marneux.marneweather.domain.cajondesastre.location.models.locationprovider.CurrentLocationProvider
import com.marneux.marneweather.domain.cajondesastre.location.models.locationprovider.CurrentLocationProviderImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module


val locationServiceModule = module {

    singleOf(::CurrentLocationProviderImpl) { bind<CurrentLocationProvider>() }
    single<ReverseGeocoder> {
        LocationReverseGeocoder(get(), get(named(KOIN_IO_COROUTINE_DISPATCHER_NAME)))
    }

}