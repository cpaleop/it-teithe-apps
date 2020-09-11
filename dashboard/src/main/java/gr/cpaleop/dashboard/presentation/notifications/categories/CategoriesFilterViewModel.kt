package gr.cpaleop.dashboard.presentation.notifications.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.entities.Category
import gr.cpaleop.dashboard.domain.usecases.GetCategoriesUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class CategoriesFilterViewModel(private val getCategoriesUseCase: GetCategoriesUseCase) :
    ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories.toSingleEvent()

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
}