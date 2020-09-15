package gr.cpaleop.categoryfilter.di

import gr.cpaleop.categoryfilter.data.AnnouncementsRepositoryImpl
import gr.cpaleop.categoryfilter.data.CategoriesRepositoryImpl
import gr.cpaleop.categoryfilter.data.mappers.AnnouncementMapper
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.categoryfilter.domain.repositories.CategoriesRepository
import gr.cpaleop.categoryfilter.domain.usecases.*
import gr.cpaleop.categoryfilter.presentation.CategoryFilterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@FlowPreview
val categoryFilterModule = module {
    viewModel { CategoryFilterViewModel(get(), get(), get()) }
    single<GetCategoryNameUseCase> { GetCategoryNameUseCaseImpl(get()) }
    single<ObserveAnnouncementsByCategoryUseCase> { ObserveAnnouncementsByCategoryUseCaseImpl(get()) }
    single<FilterAnnouncementsUseCase> { FilterAnnouncementsUseCaseImpl() }
    single { AnnouncementMapper() }
    single<AnnouncementsRepository> { AnnouncementsRepositoryImpl(get(), get(), get(), get()) }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get(), get(), get()) }
}