package gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.GetCachedCategoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CategoryFilterViewModel(
    private val getCachedCategoriesUseCase: GetCachedCategoriesUseCase,
    private val categoryFilterMapper: CategoryFilterMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categories = MutableLiveData<List<CategoryFilter>>()
    val categories: LiveData<List<CategoryFilter>> = _categories.toSingleEvent()

    private val _selectedCategories = MutableLiveData<List<String>>()
    val selectedCategories: LiveData<List<String>> = _selectedCategories.toSingleEvent()

    val submitButtonControl: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(categories) { categoryFilterList ->
                this.value = categoryFilterList.any { it.selected }
            }
        }
    }

    fun presentCategories() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _categories.value =
                    getCachedCategoriesUseCase().mapAsyncSuspended(categoryFilterMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun selectCategory(id: String) {
        viewModelScope.launch {
            _categories.value = withContext(Dispatchers.Default) {
                _categories.value?.mapAsync {
                    if (it.id == id) it.copy(selected = !it.selected)
                    else it
                }
            }
        }
    }

    fun computeSelectedCategories() {
        viewModelScope.launch {
            _selectedCategories.value = withContext(Dispatchers.Default) {
                _categories.value?.filter { it.selected }?.mapAsync { it.id }
            }
        }
    }
}