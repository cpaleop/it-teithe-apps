package gr.cpaleop.dashboard.di

import gr.cpaleop.core.data.mappers.DocumentMapper
import gr.cpaleop.core.presentation.DateFormatter
import gr.cpaleop.core.presentation.DateFormatterImpl
import gr.cpaleop.dashboard.data.*
import gr.cpaleop.dashboard.data.mappers.AnnouncementMapper
import gr.cpaleop.dashboard.data.mappers.CategoryMapper
import gr.cpaleop.dashboard.data.mappers.NotificationMapper
import gr.cpaleop.dashboard.data.mappers.ProfileMapper
import gr.cpaleop.dashboard.data.remote.NotificationsApi
import gr.cpaleop.dashboard.data.remote.ProfileApi
import gr.cpaleop.dashboard.domain.repositories.*
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementPresentationMapper
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementsViewModel
import gr.cpaleop.dashboard.presentation.files.FileDocumentMapper
import gr.cpaleop.dashboard.presentation.files.FilesViewModel
import gr.cpaleop.dashboard.presentation.notifications.NotificationPresentationMapper
import gr.cpaleop.dashboard.presentation.notifications.NotificationsViewModel
import gr.cpaleop.dashboard.presentation.notifications.categories.CategoriesFilterViewModel
import gr.cpaleop.dashboard.presentation.options.OptionsViewModel
import gr.cpaleop.dashboard.presentation.profile.ProfilePresentationMapper
import gr.cpaleop.dashboard.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val dashboardModule = module {
    viewModel { CategoriesFilterViewModel(get()) }
    viewModel { OptionsViewModel() }
    viewModel { AnnouncementsViewModel(get(), get(), get()) }
    viewModel { NotificationsViewModel(get(), get()) }
    viewModel { FilesViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    single { ProfilePresentationMapper(get()) }
    single { NotificationPresentationMapper(get()) }
    single { AnnouncementPresentationMapper(get()) }
    single<DateFormatter> { DateFormatterImpl() }
    single { FileDocumentMapper(get(), get()) }
    single { AnnouncementMapper() }
    single { NotificationMapper() }
    single { ProfileMapper(get()) }
    single { DocumentMapper() }
    single { CategoryMapper() }
    single<GetCategoriesUseCase> { GetCategoriesUseCaseImpl(get()) }
    single<SearchAnnouncementUseCase> { SearchAnnouncementUseCaseImpl(get()) }
    single<GetSavedDocumentsUseCase> { GetSavedDocumentsUseCaseImpl(get()) }
    single<GetProfileUseCase> { GetProfileUseCaseImpl(get()) }
    single<GetNotificationsUseCase> { GetNotificationsUseCaseImpl(get()) }
    single<ObserveAnnouncementsUseCase> { ObserveAnnouncementsUseCaseImpl(get()) }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get(), get(), get()) }
    single<DeviceStorageRepository> { DeviceStorageRepositoryImpl(get()) }
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