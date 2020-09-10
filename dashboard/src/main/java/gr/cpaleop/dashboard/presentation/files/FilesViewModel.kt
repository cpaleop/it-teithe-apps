package gr.cpaleop.dashboard.presentation.files

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.GetSavedDocumentsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class FilesViewModel(
    private val getSavedDocumentsUseCase: GetSavedDocumentsUseCase,
    private val fileDocumentMapper: FileDocumentMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _documents = MutableLiveData<List<FileDocument>>()
    val documents: LiveData<List<FileDocument>> = _documents.toSingleEvent()

    val documentsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(_documents) {
                this.value = it.isEmpty()
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
}