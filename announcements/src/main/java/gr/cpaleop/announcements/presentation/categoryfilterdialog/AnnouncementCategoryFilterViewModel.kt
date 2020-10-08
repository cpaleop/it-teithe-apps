package gr.cpaleop.announcements.presentation.categoryfilterdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.announcements.domain.usecases.GetCachedCategoriesUseCase
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.teithe_apps.R
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementCategoryFilterViewModel(
    private val getCachedCategoriesUseCase: GetCachedCategoriesUseCase,
    private val categoryFilterMapper: CategoryFilterMapper
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categories = MutableLiveData<List<CategoryFilter>>()
    val categories: LiveData<List<CategoryFilter>> = _categories.toSingleEvent()

    fun presentCategories() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _categories.value =
                    getCachedCategoriesUseCase().mapAsyncSuspended(categoryFilterMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }
}