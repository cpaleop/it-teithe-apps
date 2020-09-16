package gr.cpaleop.dashboard.presentation.files

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.usecases.GetFileOptionsUseCase
import gr.cpaleop.dashboard.domain.usecases.GetSavedDocumentsUseCase
import gr.cpaleop.dashboard.presentation.files.options.FileOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FilesViewModel(
    private val getSavedDocumentsUseCase: GetSavedDocumentsUseCase,
    private val fileDocumentMapper: FileDocumentMapper,
    private val getFileOptionsUseCase: GetFileOptionsUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

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
                    it.name.contains(query, true) || it.absolutePath.contains(query, true)
                })
            }
        }
    }

    fun presentFileOptions() {
        viewModelScope.launch {
            try {
                _fileOptions.value = getFileOptionsUseCase().mapAsync {
                    when (it) {
                        "Rename" -> FileOption(it, R.drawable.ic_edit)
                        "Delete" -> FileOption(it, R.drawable.ic_delete)
                        "Share" -> FileOption(it, R.drawable.ic_share)
                        "Info" -> FileOption(it, R.drawable.ic_info)
                        else -> throw IllegalArgumentException("File option $it is unknown")
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}