package gr.cpaleop.dashboard.presentation.files

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.getMimeType
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.files.options.DocumentDetails
import gr.cpaleop.dashboard.presentation.files.options.DocumentOption
import gr.cpaleop.dashboard.presentation.files.options.DocumentOptionMapper
import gr.cpaleop.dashboard.presentation.files.options.FileShareOptionData
import gr.cpaleop.dashboard.presentation.files.sort.DocumentSortOption
import gr.cpaleop.dashboard.presentation.files.sort.DocumentSortOptionMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.net.URI

class DocumentsViewModel(
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
    private val getDocumentSortUseCase: GetDocumentSortUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _refresh = MutableLiveData<Unit>()
    val refresh: LiveData<Unit> = _refresh.toSingleEvent()

    private val _document = MutableLiveData<Document>()
    val document: LiveData<Document> = _document.toSingleEvent()

    private val _documents = MutableLiveData<List<FileDocument>>()
    val documents: MediatorLiveData<List<FileDocument>> by lazy {
        MediatorLiveData<List<FileDocument>>().apply {
            addSource(_documents) {
                this.value = it
            }
        }
    }

    val documentsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(_documents) {
                this.value = it.isEmpty()
            }
        }
    }

    val documentsFilterEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(documents) {
                this.value = it.isEmpty()
            }
        }
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

    private val _optionShare = MutableLiveData<FileShareOptionData>()
    val optionShare: LiveData<FileShareOptionData> = _optionShare.toSingleEvent()

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
        }
    }

    fun presentDocuments() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _documents.value =
                    getSavedDocumentsUseCase().mapAsyncSuspended(fileDocumentMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun searchDocuments(query: String) {
        viewModelScope.launch {
            documents.value = withContext(Dispatchers.Default) {
                _documents.value?.filter {
                    it.name.contains(query, true) ||
                            it.uri.contains(query, true) ||
                            it.lastModifiedDate.contains(query, true)
                }
            }
        }
    }

    fun presentDocumentDetails(uri: String) {
        viewModelScope.launch {
            try {
                _document.value = getDocumentUseCase(uri)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentDocumentOptions() {
        viewModelScope.launch {
            try {
                _documentOptions.value =
                    getDocumentOptionsUseCase().mapAsync(documentOptionMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun handleDocumentOptionChoice(optionType: DocumentOptionType) {
        viewModelScope.launch {
            when (optionType) {
                DocumentOptionType.ANNOUNCEMENT -> {
                    _optionNavigateAnnouncement.value =
                        _document.value?.announcementId ?: return@launch
                }
                DocumentOptionType.RENAME -> {
                    _optionRename.value = DocumentDetails(
                        uri = _document.value?.uri ?: return@launch,
                        name = _document.value?.name ?: return@launch
                    )
                }
                DocumentOptionType.DELETE -> {
                    _optionDelete.value = DocumentDetails(
                        uri = document.value?.uri ?: return@launch,
                        name = document.value?.name ?: return@launch
                    )
                }
                DocumentOptionType.SHARE -> {
                    val uri = _document.value?.uri ?: return@launch
                    val fileMimeType = File(URI(uri)).getMimeType()
                    _optionShare.value = FileShareOptionData(
                        uri = _document.value?.uri ?: return@launch,
                        mimeType = fileMimeType

                    )
                }
            }
        }
    }

    fun deleteDocument(documentUri: String) {
        viewModelScope.launch {
            try {
                _refresh.value = deleteDocumentUseCase(documentUri)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun renameDocument(documentUri: String, newName: String) {
        viewModelScope.launch {
            try {
                _refresh.value = renameDocumentUseCase(documentUri, newName)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentDocumentSortSelected() {
        viewModelScope.launch {
            try {
                documentSortOptionSelected.value =
                    documentSortOptionMapper(getDocumentSortUseCase())
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentDocumentSortOptions() {
        viewModelScope.launch {
            try {
                _documentSortOptions.value = withContext(Dispatchers.Default) {
                    getDocumentSortOptionsUseCase().mapAsync(documentSortOptionMapper::invoke)
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun updateSort(@DocumentSortType type: Int, descending: Boolean, selected: Boolean) {
        viewModelScope.launch {
            try {
                val descend = if (!selected) descending else !descending
                documentSortOptionSelected.value = documentSortOptionMapper(
                    updateDocumentSortUseCase(
                        DocumentSort(
                            type = type,
                            descending = descend,
                            selected = true
                        )
                    )
                )
                _refresh.value = Unit
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}