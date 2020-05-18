package am.andranik.inc.newsrss.di

import am.andranik.inc.newsrss.repository.ArchiveRepository
import am.andranik.inc.newsrss.repository.NewsRepository
import am.andranik.inc.newsrss.repository.impl.ArchiveRepositoryImpl
import am.andranik.inc.newsrss.repository.impl.NewsRepositoryImpl
import am.andranik.inc.newsrss.repository.util.ResponseHandler
import org.koin.dsl.module

val repositoryModule = module {
    single { ResponseHandler() }

    single<NewsRepository> { NewsRepositoryImpl(get(), get(), get(), get(), get()) }
    single<ArchiveRepository> { ArchiveRepositoryImpl(get(), get(), get()) }
}