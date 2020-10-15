package gr.cpaleop.documents.di

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.documents.data.AnnouncementsRepositoryImpl
import gr.cpaleop.documents.data.DeviceStorageRepositoryImpl
import gr.cpaleop.documents.data.PreferencesRepositoryImpl
import gr.cpaleop.documents.domain.FilterStream
import gr.cpaleop.documents.domain.repositories.AnnouncementsRepository
import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository
import gr.cpaleop.documents.domain.repositories.PreferencesRepository
import gr.cpaleop.documents.domain.usecases.*
import gr.cpaleop.documents.presentation.DocumentsViewModel
import gr.cpaleop.documents.presentation.document.FileDocumentMapper
import gr.cpaleop.documents.presentation.options.DocumentOptionMapper
import gr.cpaleop.documents.presentation.sort.DocumentSortOptionMapper
import gr.cpaleop.documents.presentation.sort.DocumentSortOptionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val documentsModule = module {
    viewModel {
        DocumentsViewModel(
            get(named<MainDispatcher>()),
            get(named<DefaultDispatcher>()),
            get(),
            get(),
            get(),
            get(),
            get(),
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
    viewModel {
        DocumentSortOptionsViewModel(
            get(named<MainDispatcher>()),
            get(named<DefaultDispatcher>()),
            get(),
            get(),
            get()
        )
    }
    single { DocumentSortOptionMapper() }
    single { DocumentOptionMapper() }
    single { FileDocumentMapper(get(named<DefaultDispatcher>()), get()) }
    single<ToggleDocumentPreviewPreferenceUseCase> { ToggleDocumentPreviewPreferenceUseCaseImpl(get()) }
    single<GetDocumentPreviewPreferenceUseCase> { GetDocumentPreviewPreferenceUseCaseImpl(get()) }
    single<ObserveDocumentsAnnouncementFoldersUseCase> {
        ObserveDocumentsAnnouncementFoldersUseCaseImpl(
            get(named<DefaultDispatcher>()),
            get(),
            get(),
            get()
        )
    }
    single { FilterStream() }
    single<ObserveDocumentSortUseCase> { ObserveDocumentSortUseCaseImpl(get()) }
    single<GetDocumentSortOptionsUseCase> { GetDocumentSortOptionsUseCaseImpl(get()) }
    single<RenameDocumentUseCase> { RenameDocumentUseCaseImpl(get()) }
    single<DeleteDocumentsUseCase> { DeleteDocumentsUseCaseImpl(get()) }
    single<GetDocumentUseCase> { GetDocumentUseCaseImpl(get()) }
    single<GetDocumentOptionsUseCase> { GetDocumentOptionsUseCaseImpl() }
    single<ObserveDocumentsUseCase> {
        ObserveDocumentsUseCaseImpl(
            get(named<IODispatcher>()),
            get(),
            get(),
            get()
        )
    }
    single<DeviceStorageRepository> {
        DeviceStorageRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get()
        )
    }
    single<AnnouncementsRepository> {
        AnnouncementsRepositoryImpl(
            get(named<IODispatcher>()),
            get(),
            get()
        )
    }
    single<PreferencesRepository> {
        PreferencesRepositoryImpl(
            get(named<IODispatcher>()),
            get()
        )
    }
}