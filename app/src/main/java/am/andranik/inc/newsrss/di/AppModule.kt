package am.andranik.inc.newsrss.di

import am.andranik.inc.newsrss.data.util.SharedPreferencesHelper
import am.andranik.inc.newsrss.sync.NotificationHelper
import am.andranik.inc.newsrss.sync.SyncScheduler
import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        provideSharedPreferencesHelper(androidApplication())
    }

    single { SyncScheduler(androidApplication(), get()) }
    single { NotificationHelper(androidApplication()) }
}

private fun provideSharedPreferencesHelper(app: Application): SharedPreferencesHelper =
    SharedPreferencesHelper(
        app.getSharedPreferences(
            SharedPreferencesHelper.PREFERENCES_FILE_NAME,
            Context.MODE_PRIVATE
        )
    )
