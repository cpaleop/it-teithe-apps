package gr.cpaleop.announcements.di

import gr.cpaleop.announcements.data.AnnouncementMapper
import gr.cpaleop.announcements.data.AnnouncementsRepositoryImpl
import gr.cpaleop.announcements.data.CategoriesRepositoryImpl
import gr.cpaleop.announcements.domain.repositories.AnnouncementsRepository
import gr.cpaleop.announcements.domain.repositories.CategoriesRepository
import gr.cpaleop.announcements.domain.usecases.*
import gr.cpaleop.announcements.presentation.AnnouncementPresentationMapper
import gr.cpaleop.announcements.presentation.AnnouncementPresentationMapperImpl
import gr.cpaleop.announcements.presentation.AnnouncementsViewModel
import gr.cpaleop.announcements.presentation.categoryfilterdialog.CategoryFilterMapper
import gr.cpaleop.announcements.presentation.categoryfilterdialog.CategoryFilterViewModel
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val announcementsModule = module {
    viewModel {
        AnnouncementsViewModel(
            get(named<MainDispatcher>()),
            get(),
            get(),
            get()
        )
    }
    viewModel { CategoryFilterViewModel(get(), get()) }
    single<AnnouncementPresentationMapper> { AnnouncementPresentationMapperImpl(get()) }
    single { AnnouncementMapper() }
    single { CategoryFilterMapper() }
    single<FilterAnnouncementsUseCase> { FilterAnnouncementsUseCaseImpl(get()) }
    single<GetCachedCategoriesUseCase> { GetCachedCategoriesUseCaseImpl(get()) }
    single<ObserveAnnouncementsUseCase> { ObserveAnnouncementsUseCaseImpl(get()) }
    single<CategoriesRepository> {
        CategoriesRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
    single<AnnouncementsRepository> {
        AnnouncementsRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}