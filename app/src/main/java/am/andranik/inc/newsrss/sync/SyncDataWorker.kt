package am.andranik.inc.newsrss.sync

import am.andranik.inc.newsrss.repository.NewsRepository
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject

class SyncDataWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams), KoinComponent {

    private val newsRepository: NewsRepository by inject()
    private val notificationHelper: NotificationHelper by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val hasUpdate = newsRepository.checkUpdates()
        if (hasUpdate) {
            notificationHelper.showUpdateNotification()
        }
        Result.retry()
    }
}
