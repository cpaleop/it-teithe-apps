package gr.cpaleop.authentication.di

import gr.cpaleop.authentication.data.PreferencesRepositoryImpl
import gr.cpaleop.authentication.domain.repositories.PreferencesRepository
import gr.cpaleop.authentication.domain.usecases.BuildUriUseCase
import gr.cpaleop.authentication.domain.usecases.BuildUriUseCaseImpl
import gr.cpaleop.authentication.domain.usecases.RetrieveTokenUseCase
import gr.cpaleop.authentication.domain.usecases.RetrieveTokenUseCaseImpl
import gr.cpaleop.authentication.presentation.AuthenticationViewModel
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authenticationModule = module {
    viewModel { AuthenticationViewModel(get(named<MainDispatcher>()), get(), get()) }
    single<BuildUriUseCase> { BuildUriUseCaseImpl() }
    single<RetrieveTokenUseCase> { RetrieveTokenUseCaseImpl(get(), get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
}