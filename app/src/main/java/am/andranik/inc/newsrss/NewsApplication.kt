package am.andranik.inc.newsrss

import am.andranik.inc.newsrss.di.*
import am.andranik.inc.newsrss.sync.SyncScheduler
import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NewsApplication : Application() {

    private val syncScheduler: SyncScheduler by inject()

    override fun onCreate() {
        super.onCreate()

        initKoin()

        syncScheduler.scheduleSyncManagerIfNeeded()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@NewsApplication)
            modules(
                listOf(
                    appModule, networkModule, repositoryModule,
                    dbModule, viewModelModule, converterModule
                )
            )
        }
    }
}