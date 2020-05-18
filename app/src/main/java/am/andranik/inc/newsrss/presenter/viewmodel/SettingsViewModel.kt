package am.andranik.inc.newsrss.presenter.viewmodel

import am.andranik.inc.newsrss.data.util.SharedPreferencesHelper
import am.andranik.inc.newsrss.sync.SyncScheduler
import am.andranik.inc.newsrss.util.NewsLanguage
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val syncScheduler: SyncScheduler
) :
    ViewModel() {

    private val _language = MutableLiveData<String>().apply { value = getLanguage() }
    private val _interval = MutableLiveData<Long>().apply { value = getSyncInterval() }
    val language = _language as LiveData<String>
    val interval = _interval as LiveData<Long>

    val isSyncEnabled: Boolean
        get() = sharedPreferencesHelper.isSyncEnabled()

    fun setSyncEnabled(enabled: Boolean) {
        sharedPreferencesHelper.setSyncEnable(enabled)
        if (enabled) {
            syncScheduler.scheduleSyncManagerIfNeeded()
        } else {
            syncScheduler.cancelSyncManager()
        }
    }

    private fun getLanguage(): String {
        return sharedPreferencesHelper.getNewsLanguage()
    }

    private fun getSyncInterval(): Long {
        return sharedPreferencesHelper.getSyncInterval()
    }

    fun setInterval(selectedInterval: Long) {
        if (selectedInterval != _interval.value) {
            sharedPreferencesHelper.setSyncInterval(selectedInterval)
            _interval.value = selectedInterval
            syncScheduler.scheduleSyncManager(selectedInterval)
        }
    }

    fun setLanguage(selectedLanguage: NewsLanguage) {
        if (selectedLanguage.key != _language.value) {
            sharedPreferencesHelper.setNewsLanguage(selectedLanguage.key)
            sharedPreferencesHelper.setLastNewsDate(-1)
            _language.value = selectedLanguage.key
        }
    }
}