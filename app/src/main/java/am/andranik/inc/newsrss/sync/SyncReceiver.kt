package am.andranik.inc.newsrss.sync

import am.andranik.inc.newsrss.repository.NewsRepository
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SyncReceiver : BroadcastReceiver(), KoinComponent {

    private val newsRepository: NewsRepository by inject()
    private val notificationHelper: NotificationHelper by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i("SyncReceiver", "onReceive()")
            val hasUpdate = newsRepository.checkUpdates()
            if (hasUpdate) {
                notificationHelper.showUpdateNotification()
            }
        }
    }

}