package gr.cpaleop.announcement.di

import gr.cpaleop.announcement.data.AnnouncementMapper
import gr.cpaleop.announcement.data.AnnouncementsRepositoryImpl
import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCaseImpl
import gr.cpaleop.announcement.presentation.AnnouncementViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val announcementModule = module {
    viewModel { AnnouncementViewModel(get()) }
    single<GetAnnouncementUseCase> { GetAnnouncementUseCaseImpl(get()) }
    single { AnnouncementMapper() }
    single<AnnouncementsRepository> { AnnouncementsRepositoryImpl(get(), get(), get(), get()) }
}