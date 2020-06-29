package gr.cpaleop.dashboard.di

import gr.cpaleop.core.presentation.DateFormatter
import gr.cpaleop.core.presentation.DateFormatterImpl
import gr.cpaleop.dashboard.data.AnnouncementsRepositoryImpl
import gr.cpaleop.dashboard.data.NotificationsRepositoryImpl
import gr.cpaleop.dashboard.data.PreferencesRepositoryImpl
import gr.cpaleop.dashboard.data.ProfileRepositoryImpl
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import gr.cpaleop.dashboard.data.mappers.NotificationMapper
import gr.cpaleop.dashboard.data.mappers.ProfileMapper
import gr.cpaleop.dashboard.data.remote.NotificationsApi
import gr.cpaleop.dashboard.data.remote.ProfileApi
import gr.cpaleop.dashboard.domain.repositories.AnnouncementsRepository
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import gr.cpaleop.dashboard.domain.repositories.ProfileRepository
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementPresentationMapper
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementsViewModel
import gr.cpaleop.dashboard.presentation.notifications.NotificationPresentationMapper
import gr.cpaleop.dashboard.presentation.notifications.NotificationsViewModel
import gr.cpaleop.dashboard.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val dashboardModule = module {
    viewModel { AnnouncementsViewModel(get(), get()) }
    viewModel { NotificationsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    single { NotificationPresentationMapper(get()) }
    single { AnnouncementPresentationMapper(get()) }
    single<DateFormatter> { DateFormatterImpl() }
    single { AnnouncementMapper() }
    single { NotificationMapper() }
    single { ProfileMapper(get()) }
    single<GetProfileUseCase> { GetProfileUseCaseImpl(get()) }
    single<GetNotificationsUseCase> { GetNotificationsUseCaseImpl(get()) }
    single<ObserveAnnouncementsUseCase> { ObserveAnnouncementsUseCaseImpl(get()) }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }
    single<AnnouncementsRepository> { AnnouncementsRepositoryImpl(get(), get(), get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single { provideNotificationsApi(get()) }
    single { provideProfileApi(get()) }
}

private fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi {
    return retrofit.create(NotificationsApi::class.java)
}

private fun provideProfileApi(retrofit: Retrofit): ProfileApi {
    return retrofit.create(ProfileApi::class.java)
}