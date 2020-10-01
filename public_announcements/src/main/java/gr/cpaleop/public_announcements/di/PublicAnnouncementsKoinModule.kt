package gr.cpaleop.public_announcements.di

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.public_announcements.data.AnnouncementsRepositoryImpl
import gr.cpaleop.public_announcements.domain.repositories.AnnouncementsRepository
import gr.cpaleop.public_announcements.domain.usecases.ObservePublicAnnouncementsUseCase
import gr.cpaleop.public_announcements.domain.usecases.ObservePublicAnnouncementsUseCaseImpl
import gr.cpaleop.public_announcements.presentation.PublicAnnouncementsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val publicAnnouncementsModule = module {
    viewModel {
        PublicAnnouncementsViewModel(
            get(named<MainDispatcher>()),
            get(named<DefaultDispatcher>()),
            getScope(PublicAnnouncementsScope.ID).get(),
            get()
        )
    }
    scope(named<PublicAnnouncementsScope>()) {
        scoped<ObservePublicAnnouncementsUseCase> { ObservePublicAnnouncementsUseCaseImpl(get()) }
        scoped<AnnouncementsRepository> {
            AnnouncementsRepositoryImpl(
                get(named<IODispatcher>()),
                get(),
                get(),
                get(),
                get()
            )
        }
    }
}