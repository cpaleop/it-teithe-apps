package gr.cpaleop.create_announcement.di

import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.create_announcement.data.AttachmentsRepositoryImpl
import gr.cpaleop.create_announcement.data.CategoriesRepositoryImpl
import gr.cpaleop.create_announcement.domain.behavior.AnnouncementValidator
import gr.cpaleop.create_announcement.domain.behavior.AnnouncementValidatorImpl
import gr.cpaleop.create_announcement.domain.repositories.AttachmentsRepository
import gr.cpaleop.create_announcement.domain.repositories.CategoriesRepository
import gr.cpaleop.create_announcement.domain.usecases.*
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import gr.cpaleop.create_announcement.presentation.attachments.AttachmentPresentationMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
val createAnnouncementKoinModule = module {
    viewModel {
        CreateAnnouncementViewModel(
            get(named<MainDispatcher>()),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    single { AttachmentPresentationMapper(get(named<IODispatcher>()), get()) }
    single<AnnouncementValidator> { AnnouncementValidatorImpl() }
    single<GetCategoryUseCase> { GetCategoryUseCaseImpl(get()) }
    single<ObserveCategoriesUseCase> { ObserveCategoriesUseCaseImpl(get()) }
    single<GetSelectedAttachmentsUseCase> { GetSelectedAttachmentsUseCaseImpl(get()) }
    single<AddAttachmentsUseCase> { AddAttachmentsUseCaseImpl(get()) }
    single<RemoveAttachmentsUseCase> { RemoveAttachmentsUseCaseImpl(get()) }
    single<AttachmentsRepository> { AttachmentsRepositoryImpl(get(named<IODispatcher>()), get()) }
    single<CategoriesRepository> {
        CategoriesRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get()
        )
    }
}