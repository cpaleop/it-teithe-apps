package gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.domain.usecases.GetCachedCategoriesUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class CategoryFilterViewModel(private val getCachedCategoriesUseCase: GetCachedCategoriesUseCase) :
    ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories.toSingleEvent()

    fun presentCategories() {
        viewModelScope.launch {
            try {
                _categories.value = getCachedCategoriesUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}