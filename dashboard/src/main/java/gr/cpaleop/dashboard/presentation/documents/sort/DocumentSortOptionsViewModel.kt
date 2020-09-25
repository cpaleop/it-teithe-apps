package gr.cpaleop.dashboard.presentation.documents.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.usecases.GetDocumentSortOptionsUseCase
import gr.cpaleop.dashboard.domain.usecases.ObserveDocumentSortUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class DocumentSortOptionsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val getDocumentSortOptionsUseCase: GetDocumentSortOptionsUseCase,
    private val documentSortOptionMapper: DocumentSortOptionMapper,
    private val observeDocumentSortUseCase: ObserveDocumentSortUseCase,
) : ViewModel() {

    private val _dismissDialog = MutableLiveData<Unit>()
    val dismissDialog: LiveData<Unit> = _dismissDialog.toSingleEvent()

    private val _documentSortOptions = MutableLiveData<List<DocumentSortOption>>()
    val documentSortOptions: LiveData<List<DocumentSortOption>> =
        _documentSortOptions.toSingleEvent()

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
                observeDocumentSortUseCase.update(documentSort)
                _dismissDialog.value = Unit
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}