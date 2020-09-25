package gr.cpaleop.dashboard.presentation.documents

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.*
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType
import gr.cpaleop.dashboard.domain.entities.DocumentPreview
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.documents.document.FileDocument
import gr.cpaleop.dashboard.presentation.documents.document.FileDocumentMapper
import gr.cpaleop.dashboard.presentation.documents.options.DocumentDetails
import gr.cpaleop.dashboard.presentation.documents.options.DocumentOption
import gr.cpaleop.dashboard.presentation.documents.options.DocumentOptionMapper
import gr.cpaleop.dashboard.presentation.documents.options.DocumentShareOptionData
import gr.cpaleop.dashboard.presentation.documents.sort.DocumentSortOption
import gr.cpaleop.dashboard.presentation.documents.sort.DocumentSortOptionMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.net.URI

class DocumentsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val getSavedDocumentsUseCase: GetSavedDocumentsUseCase,
    private val fileDocumentMapper: FileDocumentMapper,
    private val getDocumentOptionsUseCase: GetDocumentOptionsUseCase,
    private val documentOptionMapper: DocumentOptionMapper,
    private val getDocumentUseCase: GetDocumentUseCase,
    private val deleteDocumentUseCase: DeleteDocumentUseCase,
    private val renameDocumentUseCase: RenameDocumentUseCase,
    private val getDocumentSortOptionsUseCase: GetDocumentSortOptionsUseCase,
    private val documentSortOptionMapper: DocumentSortOptionMapper,
    private val updateDocumentSortUseCase: UpdateDocumentSortUseCase,
    private val getDocumentSortUseCase: GetDocumentSortUseCase,
    private val getDocumentsAnnouncementFoldersUseCase: GetDocumentsAnnouncementFoldersUseCase,
    private val getDocumentPreviewPreferenceUseCase: GetDocumentPreviewPreferenceUseCase,
    private val toggleDocumentPreviewPreferenceUseCase: ToggleDocumentPreviewPreferenceUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _refresh = MutableLiveData<Unit>()
    val refresh: LiveData<Unit> = _refresh.toSingleEvent()

    private val _document = MutableLiveData<Document>()
    val document: LiveData<Document> = _document.toSingleEvent()

    private val _documentPreview = MutableLiveData<Int>()
    val documentPreview: LiveData<Int> = _documentPreview.toSingleEvent()

    private val _documentAnnouncementFolders =
        MutableLiveData<List<AnnouncementFolder>>()
    val documentAnnouncementFolders: LiveData<List<AnnouncementFolder>> =
        _documentAnnouncementFolders.toSingleEvent()

    private val _documents = MutableLiveData<List<FileDocument>>()
    val documents: MediatorLiveData<List<FileDocument>> by lazy {
        MediatorLiveData<List<FileDocument>>().apply {
            addSource(_documents) {
                this.value = it
            }
        }.toSingleMediatorEvent()
    }

    val documentsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(documents) {
                viewModelScope.launch(mainDispatcher) {
                    this@apply.value =
                        it.isEmpty() && (getDocumentPreviewPreferenceUseCase() == DocumentPreview.FILE)
                }
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

    /*
    Document Sort options
     */

    private val _documentSortOptions = MutableLiveData<List<DocumentSortOption>>()
    val documentSortOptions: LiveData<List<DocumentSortOption>> =
        _documentSortOptions/*.toSingleEvent()*/

    val documentSortOptionSelected: MediatorLiveData<DocumentSortOption> by lazy {
        MediatorLiveData<DocumentSortOption>().apply {
            addSource(_documentSortOptions) { documentSortOptionsList ->
                this.value = documentSortOptionsList.firstOrNull { it.selected } ?: return@addSource
            }
        }.toSingleMediatorEvent()
    }

    fun presentDocuments(announcementId: String?) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                val documentPreview = if (announcementId == null) {
                    getDocumentPreviewPreferenceUseCase()
                } else {
                    DocumentPreview.FILE
                }

                when (documentPreview) {
                    DocumentPreview.FILE -> {
                        _documents.value =
                            getSavedDocumentsUseCase(announcementId).mapAsyncSuspended(
                                fileDocumentMapper::invoke
                            )
                    }
                    DocumentPreview.FOLDER -> {
                        _documentAnnouncementFolders.value =
                            getDocumentsAnnouncementFoldersUseCase()
                    }
                }
                _documentPreview.value = documentPreview
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun searchDocuments(query: String) {
        viewModelScope.launch(mainDispatcher) {
            documents.value = withContext(defaultDispatcher) {
                _documents.value?.filter {
                    it.name.contains(query, true) ||
                            it.uri.contains(query, true) ||
                            it.lastModifiedDate.contains(query, true)
                }
            } ?: emptyList()
        }
    }

    fun presentDocumentDetails(uri: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _document.value = getDocumentUseCase(uri)
            } catch (t: Throwable) {
                Timber.e(t)
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
            }
        }
    }

    fun handleDocumentOptionChoice(optionType: DocumentOptionType) {
        viewModelScope.launch(mainDispatcher) {
            when (optionType) {
                DocumentOptionType.ANNOUNCEMENT -> {
                    _optionNavigateAnnouncement.value =
                        _document.value?.announcementId ?: return@launch
                }
                DocumentOptionType.RENAME -> {
                    val document = _document.value ?: return@launch
                    _optionRename.value = DocumentDetails(
                        uri = document.uri,
                        name = document.name
                    )
                }
                DocumentOptionType.DELETE -> {
                    val document = _document.value ?: return@launch
                    _optionDelete.value = DocumentDetails(
                        uri = document.uri,
                        name = document.name
                    )
                }
                DocumentOptionType.SHARE -> {
                    val uri = _document.value?.uri ?: return@launch
                    val fileMimeType = File(URI(uri)).getMimeType()
                    _optionShare.value = DocumentShareOptionData(
                        uri = _document.value?.uri ?: return@launch,
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
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun renameDocument(documentUri: String, newName: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _refresh.value = renameDocumentUseCase(documentUri, newName)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentDocumentSortSelected() {
        viewModelScope.launch(mainDispatcher) {
            try {
                documentSortOptionSelected.value =
                    documentSortOptionMapper(getDocumentSortUseCase())
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentDocumentSortOptions() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _documentSortOptions.value = withContext(defaultDispatcher) {
                    getDocumentSortOptionsUseCase().mapAsync(documentSortOptionMapper::invoke)
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun updateSort(documentSort: DocumentSort) {
        viewModelScope.launch(mainDispatcher) {
            try {
                documentSortOptionSelected.value =
                    documentSortOptionMapper(updateDocumentSortUseCase(documentSort))
                _refresh.value = Unit
            } catch (t: Throwable) {
                Timber.e(t)
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
            }
        }
    }

    fun emptyDocumentList() {
        _documents.value = emptyList()
    }
}