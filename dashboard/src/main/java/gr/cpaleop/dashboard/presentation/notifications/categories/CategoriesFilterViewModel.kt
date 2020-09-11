package gr.cpaleop.dashboard.presentation.notifications.categories

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.entities.Category
import gr.cpaleop.dashboard.domain.usecases.GetCategoriesUseCase
import gr.cpaleop.dashboard.domain.usecases.UpdateRegisteredCategoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CategoriesFilterViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val updateRegisteredCategoriesUseCase: UpdateRegisteredCategoriesUseCase
) :
    ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories.toSingleEvent()

    val resetButtonControl: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(_categories) { categories ->
                this.value = categories.any { it.isRegistered }
            }
        }
    }

    fun presentCategories() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _categories.value = getCategoriesUseCase()
            } catch (t: Throwable) {
                Timber.e(t)

            } finally {
                _loading.value = false
            }
        }
    }

    fun clearSelections() {
        viewModelScope.launch {
            _categories.value = withContext(Dispatchers.Default) {
                _categories.value?.map { it.copy(isRegistered = false) }
            }
        }
    }

    fun updateCategories(categoryId: String, isChecked: Boolean) {
        viewModelScope.launch {
            _categories.value = withContext(Dispatchers.Default) {
                _categories.value?.map {
                    var newCategory = it
                    if (it.id == categoryId) {
                        newCategory = it.copy(isRegistered = isChecked)
                    }
                    newCategory
                }
            }
        }
    }

    fun updateRegisteredCategories() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val registeredCategories =
                    _categories.value?.filter { it.isRegistered }?.mapAsync { it.id }
                        ?: return@launch
                val nonRegisteredCategories =
                    _categories.value?.filter { !it.isRegistered }?.mapAsync { it.id }
                        ?: return@launch
                updateRegisteredCategoriesUseCase(registeredCategories, nonRegisteredCategories)
                _categories.value = getCategoriesUseCase()
            } catch (t: Throwable) {
                Timber.e(t)

            } finally {
                _loading.value = false
            }
        }
    }
}