package gr.cpaleop.documents.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.*
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.core.presentation.SnackbarMessage
import gr.cpaleop.documents.R
import gr.cpaleop.documents.domain.FilterChannel
import gr.cpaleop.documents.domain.entities.AnnouncementFolder
import gr.cpaleop.documents.domain.entities.DocumentOptionType
import gr.cpaleop.documents.domain.usecases.*
import gr.cpaleop.documents.presentation.document.FileDocument
import gr.cpaleop.documents.presentation.document.FileDocumentMapper
import gr.cpaleop.documents.presentation.options.DocumentDetails
import gr.cpaleop.documents.presentation.options.DocumentOption
import gr.cpaleop.documents.presentation.options.DocumentOptionMapper
import gr.cpaleop.documents.presentation.options.DocumentShareOptionData
import gr.cpaleop.documents.presentation.sort.DocumentSortOption
import gr.cpaleop.documents.presentation.sort.DocumentSortOptionMapper
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.net.URI

@ExperimentalCoroutinesApi
class DocumentsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val observeDocumentsUseCase: ObserveDocumentsUseCase,
    private val fileDocumentMapper: FileDocumentMapper,
    private val getDocumentOptionsUseCase: GetDocumentOptionsUseCase,
    private val documentOptionMapper: DocumentOptionMapper,
    private val getDocumentUseCase: GetDocumentUseCase,
    private val deleteDocumentUseCase: DeleteDocumentUseCase,
    private val renameDocumentUseCase: RenameDocumentUseCase,
    private val documentSortOptionMapper: DocumentSortOptionMapper,
    private val observeDocumentSortUseCase: ObserveDocumentSortUseCase,
    private val observeDocumentsAnnouncementFoldersUseCase: ObserveDocumentsAnnouncementFoldersUseCase,
    private val getDocumentPreviewPreferenceUseCase: GetDocumentPreviewPreferenceUseCase,
    private val toggleDocumentPreviewPreferenceUseCase: ToggleDocumentPreviewPreferenceUseCase,
    private val filterChannel: FilterChannel
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _refresh = MutableLiveData<Unit>()
    val refresh: LiveData<Unit> = _refresh.toSingleEvent()

    private val _document = MutableLiveData<Document>()
    val document: LiveData<Document> = _document.toSingleEvent()

    private val _documentPreview = MutableLiveData<Int>()
    val documentPreview: LiveData<Int> = _documentPreview

    private val _documentAnnouncementFolders = MutableLiveData<List<AnnouncementFolder>>()
    val documentAnnouncementFolders: LiveData<List<AnnouncementFolder>> =
        _documentAnnouncementFolders.toSingleEvent()

    private val _documents = MutableLiveData<List<FileDocument>>()
    val documents: LiveData<List<FileDocument>> = _documents.toSingleEvent()

    val documentsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(documents) {
                this.value = it.isEmpty() && _documentPreview.value == DocumentPreview.FILE
            }

            addSource(documentAnnouncementFolders) {
                this.value = it.isEmpty() && _documentPreview.value == DocumentPreview.FOLDER
            }
        }.toSingleMediatorEvent()
    }

    /*
    Document Options
     */
    private val _documentOptions = MutableLiveData<List<DocumentOption>>()
    val documentOptions: LiveData<List<DocumentOption>> = _documentOptions.toSingleEvent()

    private val _optionNavigateAnnouncement = MutableLiveData<String>()
    val optionNavigateAnnouncement: LiveData<String> = _optionNavigateAnnouncement.toSingleEvent()

    private val _optionRename = MutableLiveData<DocumentDetails>()
    val optionRename: LiveData<DocumentDetails> = _optionRename.toSingleEvent()

    private val _optionDelete = MutableLiveData<DocumentDetails>()
    val optionDelete: LiveData<DocumentDetails> = _optionDelete.toSingleEvent()

    private val _optionShare = MutableLiveData<DocumentShareOptionData>()
    val optionShare: LiveData<DocumentShareOptionData> = _optionShare.toSingleEvent()

    private val _documentSortOptionSelected = MutableLiveData<DocumentSortOption>()
    val documentSortOptionSelected: LiveData<DocumentSortOption> =
        _documentSortOptionSelected.toSingleEvent()

    fun presentDocuments(announcementId: String?) {
        viewModelScope.launch(mainDispatcher) {
            try {
                var documentPreview: Int
                _documentPreview.value = getDocumentPreviewPreferenceUseCase(announcementId).also {
                    documentPreview = it
                }
                when (documentPreview) {
                    DocumentPreview.FILE -> {
                        observeDocumentsUseCase(announcementId)
                            .map { it.mapAsyncSuspended(fileDocumentMapper::invoke) }
                            .flowOn(defaultDispatcher)
                            .onStart { _loading.value = true }
                            .onEach { _loading.value = false }
                            .collect(_documents::setValue)
                    }
                    DocumentPreview.FOLDER -> {
                        observeDocumentsAnnouncementFoldersUseCase()
                            .flowOn(defaultDispatcher)
                            .onStart { _loading.value = true }
                            .onEach { _loading.value = false }
                            .collect(_documentAnnouncementFolders::setValue)
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun filter(query: String) {
        viewModelScope.launch(mainDispatcher) {
            filterChannel.value = query
        }
    }

    fun presentDocumentDetails(uri: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _document.value = getDocumentUseCase(uri)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }

    fun presentDocumentOptions() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _documentOptions.value =
                    getDocumentOptionsUseCase().mapAsync(documentOptionMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }

    fun handleDocumentOptionChoice(optionType: DocumentOptionType) {
        viewModelScope.launch(mainDispatcher) {
            val document = _document.value ?: return@launch
            when (optionType) {
                DocumentOptionType.ANNOUNCEMENT -> {
                    _optionNavigateAnnouncement.value = document.announcementId
                }
                DocumentOptionType.RENAME -> {
                    _optionRename.value = DocumentDetails(
                        uri = document.uri,
                        name = document.name
                    )
                }
                DocumentOptionType.DELETE -> {
                    _optionDelete.value = DocumentDetails(
                        uri = document.uri,
                        name = document.name
                    )
                }
                DocumentOptionType.SHARE -> {
                    val uri = document.uri
                    val fileMimeType = File(URI(uri)).getMimeType()
                    _optionShare.value = DocumentShareOptionData(
                        uri = uri,
                        mimeType = fileMimeType

                    )
                }
            }
        }
    }

    fun deleteDocument(documentUri: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _refresh.value = deleteDocumentUseCase(documentUri)
                _message.value = SnackbarMessage(R.string.documents_delete_success_message)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }

    fun renameDocument(documentUri: String, newName: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _refresh.value = renameDocumentUseCase(documentUri, newName)
                _message.value = SnackbarMessage(R.string.documents_rename_success_message, newName)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }

    fun presentDocumentSortSelected() {
        viewModelScope.launch(mainDispatcher) {
            try {
                observeDocumentSortUseCase()
                    .map(documentSortOptionMapper::invoke)
                    .flowOn(defaultDispatcher)
                    .collect(_documentSortOptionSelected::setValue)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }

    fun togglePreview() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _documentPreview.value = toggleDocumentPreviewPreferenceUseCase()
                _refresh.value = Unit
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }
}