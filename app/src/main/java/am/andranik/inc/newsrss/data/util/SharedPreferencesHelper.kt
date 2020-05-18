package am.andranik.inc.newsrss.data.util

import am.andranik.inc.newsrss.util.NewsLanguage
import am.andranik.inc.newsrss.util.SyncInterval
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesHelper(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val PREFERENCES_FILE_NAME = "am.andranik.inc.newsrss.shared_prefs"

        private const val LANGUAGE_PREF_KEY = "language.pref.key"
        private const val LAST_NEWS_DATE_PREF_KEY = "last.news.date.pref.key"
        private const val SYNC_INTERVAL_PREF_KEY = "sync.interval.pref.key"
        private const val SYNC_ENABLE_PREF_KEY = "sync.enable.pref.key"
    }

    fun getNewsLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_PREF_KEY, NewsLanguage.ARM.key)
            ?: NewsLanguage.ARM.key
    }

    fun setNewsLanguage(newsLanguage: String) {
        sharedPreferences.edit(commit = true) {
            putString(LANGUAGE_PREF_KEY, newsLanguage)
        }
    }

    fun getLastNewsDate(): Long {
        return sharedPreferences.getLong(LAST_NEWS_DATE_PREF_KEY, -1)
    }

    fun setLastNewsDate(lastNewsDate: Long) {
        sharedPreferences.edit(commit = true) {
            putLong(LAST_NEWS_DATE_PREF_KEY, lastNewsDate)
        }
    }

    fun getSyncInterval(): Long {
        return sharedPreferences.getLong(SYNC_INTERVAL_PREF_KEY, SyncInterval.MIN15.milis)
    }

    fun setSyncInterval(syncInterval: Long) {
        sharedPreferences.edit(commit = true) {
            putLong(SYNC_INTERVAL_PREF_KEY, syncInterval)
        }
    }

    fun isSyncEnabled(): Boolean {
        return sharedPreferences.getBoolean(SYNC_ENABLE_PREF_KEY, true)
    }

    fun setSyncEnable(syncEnable: Boolean) {
        sharedPreferences.edit(commit = true) {
            putBoolean(SYNC_ENABLE_PREF_KEY, syncEnable)
        }
    }
}