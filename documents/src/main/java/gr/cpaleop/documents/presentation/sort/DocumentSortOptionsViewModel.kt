package gr.cpaleop.documents.presentation.sort

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.usecases.GetDocumentSortOptionsUseCase
import gr.cpaleop.documents.domain.usecases.ObserveDocumentSortUseCase
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
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
) : BaseViewModel() {

    private val _dismissDialog = MutableLiveData<Unit>()
    val dismissDialog: LiveData<Unit> = _dismissDialog.toSingleEvent()

    private val _documentSortOptions =
        MutableLiveData<List<DocumentSortOption>>()
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