package com.marneux.marneweather.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val KOIN_IO_COROUTINE_DISPATCHER_NAME = "io_coroutine_dispatcher"

val coroutineDispatchersModule = module {
    single(named(KOIN_IO_COROUTINE_DISPATCHER_NAME)) { Dispatchers.IO }
}