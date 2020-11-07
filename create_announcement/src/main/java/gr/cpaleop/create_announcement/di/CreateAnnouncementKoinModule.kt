package gr.cpaleop.create_announcement.di

import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val createAnnouncementKoinModule = module {
    viewModel { CreateAnnouncementViewModel(get(named<MainDispatcher>()), get(), get(), get()) }
}