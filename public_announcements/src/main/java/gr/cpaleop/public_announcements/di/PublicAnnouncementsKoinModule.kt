package gr.cpaleop.public_announcements.di

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.public_announcements.presentation.PublicAnnouncementsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val publicAnnouncementsModule = module {
    scope<PublicAnnouncementsScope> {
        viewModel {
            PublicAnnouncementsViewModel(
                get(named<MainDispatcher>()),
                get(named<DefaultDispatcher>()),
                get(),
                get()
            )
        }
    }
}