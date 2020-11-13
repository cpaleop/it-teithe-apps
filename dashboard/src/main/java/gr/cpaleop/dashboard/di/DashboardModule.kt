package gr.cpaleop.dashboard.di

import gr.cpaleop.core.data.mappers.DocumentMapper
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.dashboard.data.CategoriesRepositoryImpl
import gr.cpaleop.dashboard.data.NotificationsRepositoryImpl
import gr.cpaleop.dashboard.data.mappers.NotificationMapper
import gr.cpaleop.dashboard.data.remote.NotificationsApi
import gr.cpaleop.dashboard.domain.repositories.CategoriesRepository
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository
import gr.cpaleop.dashboard.domain.usecases.*
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
    viewModel { CategoriesFilterViewModel(get(), get()) }
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
    single { NotificationMapper(get(named<DefaultDispatcher>())) }
    single { DocumentMapper(get(named<DefaultDispatcher>())) }
    single<ReadAllNotificationsUseCase> { ReadAllNotificationsUseCaseImpl(get()) }
    single<UpdateRegisteredCategoriesUseCase> { UpdateRegisteredCategoriesUseCaseImpl(get()) }
    single<ObserveCategoriesUseCase> { ObserveCategoriesUseCaseImpl(get()) }
    single<ObserveNotificationsUseCase> { ObserveNotificationsUseCaseImpl(get()) }
    single<CategoriesRepository> {
        CategoriesRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get()
        )
    }
    single<NotificationsRepository> { NotificationsRepositoryImpl(get(), get()) }
    single { provideNotificationsApi(get()) }
}

private fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi {
    return retrofit.create(NotificationsApi::class.java)
}