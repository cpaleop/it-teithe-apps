package gr.cpaleop.teithe_apps.di

import gr.cpaleop.teithe_apps.data.PreferencesRepositoryImpl
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
import gr.cpaleop.teithe_apps.domain.repositories.PreferencesRepository
import gr.cpaleop.teithe_apps.domain.usecases.AuthenticatedUseCase
import gr.cpaleop.teithe_apps.domain.usecases.AuthenticatedUseCaseImpl
import gr.cpaleop.teithe_apps.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(get(named<MainDispatcher>()), get()) }
    single<AuthenticatedUseCase> { AuthenticatedUseCaseImpl(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
}