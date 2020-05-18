package am.andranik.inc.newsrss.di

import am.andranik.inc.newsrss.data.converters.DateConverter
import am.andranik.inc.newsrss.data.converters.NewsConverter
import org.koin.dsl.module

val converterModule = module {

    single { NewsConverter(get()) }

    single { DateConverter() }
}

