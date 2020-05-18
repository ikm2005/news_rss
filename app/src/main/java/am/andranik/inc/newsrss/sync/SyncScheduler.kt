package am.andranik.inc.newsrss.sync

import am.andranik.inc.newsrss.data.util.SharedPreferencesHelper
import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit


class SyncScheduler(
    private val context: Context,
    private val preferencesHelper: SharedPreferencesHelper
) {

    companion object {
        private const val TAG_SYNC_DATA = "sync.news.updates"
        private const val SYNC_DATA_WORK_NAME = "news.sync.data.worker"
    }

    fun scheduleSyncManagerIfNeeded() {
        if (preferencesHelper.isSyncEnabled()) {
            scheduleSyncManager(preferencesHelper.getSyncInterval())
        }
    }

    fun scheduleSyncManager(frequency: Long) {
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncDataWork =
            PeriodicWorkRequest.Builder(SyncDataWorker::class.java, frequency, TimeUnit.MILLISECONDS)
                .addTag(TAG_SYNC_DATA)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicSyncDataWork
        )
    }

    fun cancelSyncManager() {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG_SYNC_DATA)
    }
}