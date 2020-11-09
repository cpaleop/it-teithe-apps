package gr.cpaleop.create_announcement.di

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.create_announcement.data.CategoriesRepositoryImpl
import gr.cpaleop.create_announcement.domain.repositories.CategoriesRepository
import gr.cpaleop.create_announcement.domain.usecases.*
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val createAnnouncementKoinModule = module {
    viewModel { CreateAnnouncementViewModel(get(named<MainDispatcher>()), get(), get(), get()) }
    single<CreateAnnouncementUseCase> { CreateAnnouncementUseCaseImpl() }
    single<GetCategoryUseCase> { GetCategoryUseCaseImpl(get()) }
    single<GetCategoriesUseCase> { GetCategoriesUseCaseImpl(get()) }
    single<CategoriesRepository> {
        CategoriesRepositoryImpl(
            get(named<IODispatcher>()), get(),
            get()
        )
    }
}