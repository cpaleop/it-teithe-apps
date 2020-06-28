package gr.cpaleop.dashboard.di

import gr.cpaleop.core.presentation.DateFormatter
import gr.cpaleop.core.presentation.DateFormatterImpl
import gr.cpaleop.dashboard.data.AnnouncementsRepositoryImpl
import gr.cpaleop.dashboard.data.NotificationsRepositoryImpl
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import gr.cpaleop.dashboard.data.mappers.NotificationMapper
import gr.cpaleop.dashboard.data.remote.NotificationsApi
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCaseImpl
import gr.cpaleop.dashboard.domain.usecases.ObserveAnnouncementsUseCase
import gr.cpaleop.dashboard.domain.usecases.ObserveAnnouncementsUseCaseImpl
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementPresentationMapper
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementsViewModel
import gr.cpaleop.dashboard.presentation.notifications.NotificationPresentationMapper
import gr.cpaleop.dashboard.presentation.notifications.NotificationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val dashboardModule = module {
    viewModel { AnnouncementsViewModel(get(), get()) }
    viewModel { NotificationsViewModel(get(), get()) }
    single { NotificationPresentationMapper() }
    single { AnnouncementPresentationMapper(get()) }
    single<DateFormatter> { DateFormatterImpl() }
    single { AnnouncementMapper() }
    single { NotificationMapper() }
    single<GetNotificationsUseCase> { GetNotificationsUseCaseImpl(get()) }
    single<ObserveAnnouncementsUseCase> { ObserveAnnouncementsUseCaseImpl(get()) }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }
    single<AnnouncementsRepository> { AnnouncementsRepositoryImpl(get(), get(), get(), get()) }
    single { provideNotificationsApi(get()) }
}

private fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi {
    return retrofit.create(NotificationsApi::class.java)
}