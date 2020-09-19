package gr.cpaleop.dashboard.presentation.files

import android.webkit.MimeTypeMap
import androidx.lifecycle.*
import gr.cpaleop.common.extensions.getMimeType
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.FileOptionType
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.files.options.FileDetails
import gr.cpaleop.dashboard.presentation.files.options.FileOption
import gr.cpaleop.dashboard.presentation.files.options.FileOptionMapper
import gr.cpaleop.dashboard.presentation.files.options.FileShareOptionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.net.URI


class FilesViewModel(
    private val getSavedDocumentsUseCase: GetSavedDocumentsUseCase,
    private val fileDocumentMapper: FileDocumentMapper,
    private val getFileOptionsUseCase: GetFileOptionsUseCase,
    private val fileOptionMapper: FileOptionMapper,
    private val getDocumentUseCase: GetDocumentUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val renameFileUseCase: RenameFileUseCase
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
    FileOptions
     */

    private val _fileOptions = MutableLiveData<List<FileOption>>()
    val fileOptions: LiveData<List<FileOption>> = _fileOptions.toSingleEvent()

    private val _optionNavigateAnnouncement = MutableLiveData<String>()
    val optionNavigateAnnouncement: LiveData<String> = _optionNavigateAnnouncement.toSingleEvent()

    private val _optionRename = MutableLiveData<FileDetails>()
    val optionRename: LiveData<FileDetails> = _optionRename.toSingleEvent()

    private val _optionDelete = MutableLiveData<FileDetails>()
    val optionDelete: LiveData<FileDetails> = _optionDelete.toSingleEvent()

    private val _optionInfo = MutableLiveData<String>()
    val optionInfo: LiveData<String> = _optionInfo.toSingleEvent()

    private val _optionShare = MutableLiveData<FileShareOptionData>()
    val optionShare: LiveData<FileShareOptionData> = _optionShare.toSingleEvent()

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
                _fileOptions.value = getFileOptionsUseCase().mapAsync(fileOptionMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun handleFileOptionChoice(optionType: FileOptionType) {
        viewModelScope.launch {
            when (optionType) {
                FileOptionType.ANNOUNCEMENT -> {
                    _optionNavigateAnnouncement.value =
                        _document.value?.announcementId ?: return@launch
                }
                FileOptionType.RENAME -> {
                    _optionRename.value = FileDetails(
                        uri = _document.value?.uri ?: return@launch,
                        name = _document.value?.name ?: return@launch
                    )
                }
                FileOptionType.DELETE -> {
                    _optionDelete.value = FileDetails(
                        uri = document.value?.uri ?: return@launch,
                        name = document.value?.name ?: return@launch
                    )
                }
                FileOptionType.SHARE -> {
                    val uri = _document.value?.uri ?: return@launch
                    val fileMimeType = File(URI(uri)).getMimeType()
                    _optionShare.value = FileShareOptionData(
                        uri = _document.value?.uri ?: return@launch,
                        mimeType = fileMimeType

                    )
                }
                FileOptionType.INFO -> {
                    _optionInfo.value = _document.value?.uri ?: return@launch
                }
            }
        }
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun deleteFile(fileUri: String) {
        viewModelScope.launch {
            try {
                _refresh.value = deleteFileUseCase(fileUri)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun renameFile(fileUri: String, newName: String) {
        viewModelScope.launch {
            try {
                _refresh.value = renameFileUseCase(fileUri, newName)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}