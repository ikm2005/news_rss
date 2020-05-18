package am.andranik.inc.newsrss.di

import am.andranik.inc.newsrss.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single { AppDatabase.buildDatabase(androidContext()) }
    factory { get<AppDatabase>().newsDao() }
}