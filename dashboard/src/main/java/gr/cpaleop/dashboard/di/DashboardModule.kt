package gr.cpaleop.dashboard.di

import gr.cpaleop.core.data.mappers.DocumentMapper
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.DateFormatterImpl
import gr.cpaleop.dashboard.data.AnnouncementsRepositoryImpl
import gr.cpaleop.dashboard.data.CategoriesRepositoryImpl
import gr.cpaleop.dashboard.data.NotificationsRepositoryImpl
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import gr.cpaleop.dashboard.data.mappers.CategoryMapper
import gr.cpaleop.dashboard.data.mappers.NotificationMapper
import gr.cpaleop.dashboard.data.remote.NotificationsApi
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementPresentationMapper
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementPresentationMapperImpl
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementsViewModel
import gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog.CategoryFilterMapper
import gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog.CategoryFilterViewModel
import gr.cpaleop.dashboard.presentation.notifications.NotificationPresentationMapper
import gr.cpaleop.dashboard.presentation.notifications.NotificationsViewModel
import gr.cpaleop.dashboard.presentation.notifications.categories.CategoriesFilterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

@ExperimentalCoroutinesApi
val dashboardModule = module {
    viewModel { CategoryFilterViewModel(get(), get()) }
    viewModel { CategoriesFilterViewModel(get(), get()) }
    viewModel { AnnouncementsViewModel(get(named<MainDispatcher>()), get(), get(), get()) }
    viewModel {
        NotificationsViewModel(
            get(named<MainDispatcher>()),
            get(named<DefaultDispatcher>()),
            get(),
            get(),
            get()
        )
    }
    single { NotificationPresentationMapper(get()) }
    single<AnnouncementPresentationMapper> { AnnouncementPresentationMapperImpl(get()) }
    single<DateFormatter> { DateFormatterImpl() }
    single { CategoryFilterMapper() }
    single { AnnouncementMapper() }
    single { NotificationMapper(get(named<DefaultDispatcher>())) }
    single { DocumentMapper(get(named<DefaultDispatcher>())) }
    single { CategoryMapper() }
    single<ReadAllNotificationsUseCase> { ReadAllNotificationsUseCaseImpl(get()) }
    single<FilterAnnouncementsUseCase> { FilterAnnouncementsUseCaseImpl(get()) }
    single<GetCachedCategoriesUseCase> { GetCachedCategoriesUseCaseImpl(get()) }
    single<UpdateRegisteredCategoriesUseCase> { UpdateRegisteredCategoriesUseCaseImpl(get()) }
    single<GetCategoriesUseCase> { GetCategoriesUseCaseImpl(get()) }
    single<GetNotificationsUseCase> { GetNotificationsUseCaseImpl(get()) }
    single<ObserveAnnouncementsUseCase> { ObserveAnnouncementsUseCaseImpl(get()) }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get(), get(), get(), get()) }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }
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
    single { provideNotificationsApi(get()) }
}

private fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi {
    return retrofit.create(NotificationsApi::class.java)
}