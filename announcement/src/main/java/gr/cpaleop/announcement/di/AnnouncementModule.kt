package gr.cpaleop.announcement.di

import gr.cpaleop.announcement.data.AnnouncementMapper
import gr.cpaleop.announcement.data.AnnouncementsRepositoryImpl
import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCaseImpl
import gr.cpaleop.announcement.domain.usecases.ObserveDownloadNotifierUseCase
import gr.cpaleop.announcement.domain.usecases.ObserveDownloadNotifierUseCaseImpl
import gr.cpaleop.announcement.presentation.AnnouncementDetailsMapper
import gr.cpaleop.announcement.presentation.AnnouncementDetailsMapperImpl
import gr.cpaleop.announcement.presentation.AnnouncementViewModel
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val announcementModule = module {
    viewModel { AnnouncementViewModel(get(named<MainDispatcher>()), get(), get(), get()) }
    single<AnnouncementDetailsMapper> {
        AnnouncementDetailsMapperImpl(
            get(named<DefaultDispatcher>()),
            get()
        )
    }
    single<ObserveDownloadNotifierUseCase> { ObserveDownloadNotifierUseCaseImpl(get()) }
    single<GetAnnouncementUseCase> { GetAnnouncementUseCaseImpl(get()) }
    single { AnnouncementMapper() }
    single<AnnouncementsRepository> { AnnouncementsRepositoryImpl(get(), get(), get(), get()) }
}