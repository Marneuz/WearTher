package com.marneux.marneweather.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.marneux.marneweather.data.local.textgeneration.GeneratedTextCacheDatabaseDao
import com.marneux.marneweather.data.local.weather.DatabaseDao
import com.marneux.marneweather.di.KOIN_IO_COROUTINE_DISPATCHER_NAME
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class CleanupWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val savedWeatherDetailsDao: DatabaseDao by inject()
    private val generatedTextCacheDao: GeneratedTextCacheDatabaseDao by inject()
    private val ioDispatcher: CoroutineDispatcher by inject(named(KOIN_IO_COROUTINE_DISPATCHER_NAME))

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        try {
            savedWeatherDetailsDao.deleteAllItemsMarkedAsDeleted()
            generatedTextCacheDao.deleteAllSavedText()
            Result.success()
        } catch (exception: Exception) {
            if (exception is CancellationException) throw exception
            Result.failure()
        }
    }
}