package gr.cpaleop.categoryfilter.di

import gr.cpaleop.categoryfilter.data.AnnouncementsRepositoryImpl
import gr.cpaleop.categoryfilter.data.CategoriesRepositoryImpl
import gr.cpaleop.categoryfilter.data.mappers.AnnouncementMapper
import gr.cpaleop.categoryfilter.domain.repositories.AnnouncementsRepository
import gr.cpaleop.categoryfilter.domain.repositories.CategoriesRepository
import gr.cpaleop.categoryfilter.domain.usecases.GetAnnouncementsByCategoryUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetAnnouncementsByCategoryUseCaseImpl
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCaseImpl
import gr.cpaleop.categoryfilter.presentation.CategoryFilterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoryFilterModule = module {
    viewModel { CategoryFilterViewModel(get(), get()) }
    single<GetCategoryNameUseCase> { GetCategoryNameUseCaseImpl(get()) }
    single<GetAnnouncementsByCategoryUseCase> { GetAnnouncementsByCategoryUseCaseImpl(get()) }
    single { AnnouncementMapper() }
    single<AnnouncementsRepository> { AnnouncementsRepositoryImpl(get(), get(), get()) }
    single<CategoriesRepository> { CategoriesRepositoryImpl(get(), get()) }
}