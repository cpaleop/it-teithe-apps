package gr.cpaleop.announcement.di

import gr.cpaleop.announcement.data.AnnouncementMapper
import gr.cpaleop.announcement.data.AnnouncementsRepositoryImpl
import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.announcement.domain.usecases.*
import gr.cpaleop.announcement.presentation.AnnouncementDetailsMapper
import gr.cpaleop.announcement.presentation.AnnouncementDetailsMapperImpl
import gr.cpaleop.announcement.presentation.AnnouncementViewModel
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val announcementModule = module {
    viewModel {
        AnnouncementViewModel(
            get(named<MainDispatcher>()),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single<AnnouncementDetailsMapper> {
        AnnouncementDetailsMapperImpl(
            get(named<DefaultDispatcher>()),
            get()
        )
    }
    single<ObserveFavoriteUseCase> { ObserveFavoriteUseCaseImpl(get()) }
    single<ToggleFavoriteUseCase> { ToggleFavoriteUseCaseImpl(get()) }
    single<ObserveDownloadNotifierUseCase> { ObserveDownloadNotifierUseCaseImpl(get()) }
    single<GetAnnouncementUseCase> { GetAnnouncementUseCaseImpl(get()) }
    single { AnnouncementMapper() }
    single<AnnouncementsRepository> {
        AnnouncementsRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get(),
            get()
        )
    }
}