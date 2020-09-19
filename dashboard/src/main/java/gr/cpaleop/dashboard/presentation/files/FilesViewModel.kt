package gr.cpaleop.dashboard.presentation.files

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.usecases.GetDocumentUseCase
import gr.cpaleop.dashboard.domain.usecases.GetFileOptionsUseCase
import gr.cpaleop.dashboard.domain.usecases.GetSavedDocumentsUseCase
import gr.cpaleop.dashboard.presentation.files.options.FileOption
import gr.cpaleop.dashboard.presentation.files.options.FileOptionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FilesViewModel(
    private val getSavedDocumentsUseCase: GetSavedDocumentsUseCase,
    private val fileDocumentMapper: FileDocumentMapper,
    private val getFileOptionsUseCase: GetFileOptionsUseCase,
    private val getDocumentUseCase: GetDocumentUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _document = MutableLiveData<Document>()
    val document: LiveData<Document> = _document.toSingleEvent()

    private val _navigateAnnouncement = MutableLiveData<String>()
    val navigateAnnouncement: LiveData<String> = _navigateAnnouncement.toSingleEvent()

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

    private val _fileOptions = MutableLiveData<List<FileOption>>()
    val fileOptions: LiveData<List<FileOption>> = _fileOptions.toSingleEvent()

    private val _fileOptionsAnnouncement = MutableLiveData<List<FileOption>>()
    val fileOptionsAnnouncement: LiveData<List<FileOption>> =
        _fileOptionsAnnouncement.toSingleEvent()

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
            withContext(Dispatchers.Default) {
                documents.postValue(_documents.value?.filter {
                    it.name.contains(query, true) || it.uri.contains(query, true)
                })
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

    fun presentFileOptions() {
        viewModelScope.launch {
            try {
                val options = getFileOptionsUseCase().mapAsync {
                    when (it) {
                        "Rename" -> FileOption(FileOptionType.FILE, it, R.drawable.ic_edit)
                        "Delete" -> FileOption(FileOptionType.FILE, it, R.drawable.ic_delete)
                        "Share" -> FileOption(FileOptionType.FILE, it, R.drawable.ic_share)
                        "Info" -> FileOption(FileOptionType.FILE, it, R.drawable.ic_info)
                        "Go to announcement" -> FileOption(
                            FileOptionType.ANNOUNCEMENT,
                            it,
                            R.drawable.ic_link
                        )
                        else -> throw IllegalArgumentException("File option $it is unknown")
                    }
                }

                _fileOptions.value = options.filter { it.type == FileOptionType.FILE }
                _fileOptionsAnnouncement.value =
                    options.filter { it.type == FileOptionType.ANNOUNCEMENT }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun getAnnouncementId() {
        _navigateAnnouncement.value = _document.value?.announcementId ?: return
    }
}