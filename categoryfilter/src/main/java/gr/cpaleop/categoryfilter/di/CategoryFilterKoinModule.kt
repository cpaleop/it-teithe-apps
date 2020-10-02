package gr.cpaleop.categoryfilter.di

import gr.cpaleop.categoryfilter.data.AnnouncementsRepositoryImpl
import gr.cpaleop.categoryfilter.data.CategoriesRepositoryImpl
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.categoryfilter.domain.repositories.CategoriesRepository
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCaseImpl
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCaseImpl
import gr.cpaleop.categoryfilter.presentation.CategoryFilterViewModel
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val categoryFilterModule = module {
    viewModel {
        CategoryFilterViewModel(
            get(named<MainDispatcher>()),
            get(named<DefaultDispatcher>()),
            get(),
            get(),
            get()
        )
    }
    single<GetCategoryNameUseCase> { GetCategoryNameUseCaseImpl(get()) }
    single<ObserveAnnouncementsByCategoryUseCase> { ObserveAnnouncementsByCategoryUseCaseImpl(get()) }
    single<AnnouncementsRepository> {
        AnnouncementsRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get(),
            get(),
            get()
        )
    }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get(), get(), get()) }
}