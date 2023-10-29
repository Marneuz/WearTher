package com.marneux.marneweather.di

import com.marneux.marneweather.data.remote.languagemodel.TextGeneratorClient
import com.marneux.marneweather.data.remote.languagemodel.TextGeneratorClientConstants
import com.marneux.marneweather.data.remote.languagemodel.TextGeneratorClientConstants.OPEN_AI_API_TOKEN
import com.marneux.marneweather.data.remote.location.LocationClient
import com.marneux.marneweather.data.remote.location.LocationClientConstants
import com.marneux.marneweather.data.remote.weather.WeatherClient
import com.marneux.marneweather.data.remote.weather.WeatherClientConstants
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {

    single { NetworkModule.provideLocationClient() }
    single { NetworkModule.provideWeatherClient() }
    single { NetworkModule.provideTextGeneratorClient() }

}

object NetworkModule {

    fun provideWeatherClient(): WeatherClient = Retrofit.Builder()
        .baseUrl(WeatherClientConstants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(WeatherClient::class.java)


    fun provideLocationClient(): LocationClient {

        return Retrofit.Builder()
            .baseUrl(LocationClientConstants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(LocationClient::class.java)
    }

    fun provideTextGeneratorClient(): TextGeneratorClient = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer ${OPEN_AI_API_TOKEN}")
                        .build()
                    chain.proceed(newRequest)
                }
                .build()
        )
        .baseUrl(TextGeneratorClientConstants.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(TextGeneratorClient::class.java)

}