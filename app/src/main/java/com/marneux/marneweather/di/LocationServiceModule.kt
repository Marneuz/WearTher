package com.marneux.marneweather.di

import com.marneux.marneweather.data.location.CurrentLocationRepositoryImpl
import com.marneux.marneweather.data.location.GeocoderImpl
import com.marneux.marneweather.domain.repositories.location.CurrentLocationRepository
import com.marneux.marneweather.domain.repositories.location.GeocoderRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module


val locationServiceModule = module {

    singleOf(::CurrentLocationRepositoryImpl) { bind<CurrentLocationRepository>() }
    single<GeocoderRepository> {
        GeocoderImpl(get(), get(named(KOIN_IO_COROUTINE_DISPATCHER_NAME)))
    }

}