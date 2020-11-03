package gr.cpaleop.favorites.di

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.favorites.data.AnnouncementsRepositoryImpl
import gr.cpaleop.favorites.domain.repositories.AnnouncementsRepository
import gr.cpaleop.favorites.domain.usecases.ObserveFavoriteAnnouncementsUseCase
import gr.cpaleop.favorites.domain.usecases.ObserveFavoriteAnnouncementsUseCaseImpl
import gr.cpaleop.favorites.presentation.FavoritesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val favoritesKoinModule = module {
    viewModel {
        FavoritesViewModel(
            get(named<MainDispatcher>()),
            get(named<DefaultDispatcher>()),
            get(),
            get()
        )
    }
    single<ObserveFavoriteAnnouncementsUseCase> { ObserveFavoriteAnnouncementsUseCaseImpl(get()) }
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