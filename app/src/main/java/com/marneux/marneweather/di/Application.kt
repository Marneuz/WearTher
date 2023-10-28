package com.marneux.marneweather.di

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.marneux.marneweather.data.workers.CleanupWorker
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.concurrent.TimeUnit

class Application : Application(), KoinComponent{

    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            workManagerFactory()
            modules(
                viewModelsModule,
                networkModule,
                databaseModule,
                repositoriesModule,
                locationServiceModule,
                coroutineDispatchersModule
            )
        }
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        enqueueDeleteMarkedItemsWorker()
    }

    private fun enqueueDeleteMarkedItemsWorker() {

        val periodicWorkRequest = PeriodicWorkRequestBuilder<CleanupWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DELETE_MARKED_ITEMS_WORK_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

    }

    companion object {
        private const val DELETE_MARKED_ITEMS_WORK_ID =
            "com.marneux.marneweather.data.workers.DeleteMarkedItemsWorker"
    }
}