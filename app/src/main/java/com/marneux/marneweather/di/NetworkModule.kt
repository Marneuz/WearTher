package com.marneux.marneweather.di

import com.marneux.marneweather.data.generatedsummary.remote.TextGeneratorClient
import com.marneux.marneweather.data.generatedsummary.remote.TextGeneratorClientConstants
import com.marneux.marneweather.data.generatedsummary.remote.TextGeneratorClientConstants.OPEN_AI_API_TOKEN
import com.marneux.marneweather.data.location.remote.LocationClient
import com.marneux.marneweather.data.location.remote.LocationClientConstants
import com.marneux.marneweather.data.weather.remote.WeatherClient
import com.marneux.marneweather.data.weather.remote.WeatherClientConstants
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single { NetworkModule.provideLocationClient() }
    single { NetworkModule.provideWeatherClient() }
    single { NetworkModule.provideTextGeneratorClient() }

}

object NetworkModule {

    fun provideWeatherClient(): WeatherClient = Retrofit.Builder()
        .baseUrl(WeatherClientConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherClient::class.java)


    fun provideLocationClient(): LocationClient {

        return Retrofit.Builder()
            .baseUrl(LocationClientConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TextGeneratorClient::class.java)

}