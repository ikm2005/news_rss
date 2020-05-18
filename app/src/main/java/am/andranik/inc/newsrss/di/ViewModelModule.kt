package am.andranik.inc.newsrss.di

import am.andranik.inc.newsrss.presenter.viewmodel.ArchiveViewModel
import am.andranik.inc.newsrss.presenter.viewmodel.NewsViewModel
import am.andranik.inc.newsrss.presenter.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { NewsViewModel(get()) }
    viewModel { ArchiveViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
}