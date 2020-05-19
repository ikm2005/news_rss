package am.andranik.inc.newsrss.sync

import am.andranik.inc.newsrss.data.util.SharedPreferencesHelper
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock


class SyncScheduler(
    private val context: Context,
    private val preferencesHelper: SharedPreferencesHelper
) {

    private var alarmMgr: AlarmManager? = null
    private val alarmIntent: PendingIntent

    init {
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, SyncReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }
    }

    fun scheduleSyncManagerIfNeeded() {
        if (preferencesHelper.isSyncEnabled()) {
            scheduleSyncManager(preferencesHelper.getSyncInterval())
        }
    }

    fun scheduleSyncManager(frequency: Long) {
        alarmMgr?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + frequency,
            frequency,
            alarmIntent
        )
    }

    fun cancelSyncManager() {
        alarmMgr?.cancel(alarmIntent)
    }
}