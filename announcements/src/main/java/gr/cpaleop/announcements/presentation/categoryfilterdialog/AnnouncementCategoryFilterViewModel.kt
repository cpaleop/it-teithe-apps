package gr.cpaleop.announcements.presentation.categoryfilterdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.announcements.domain.usecases.ObserveCategoriesUseCase
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.teithe_apps.R
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementCategoryFilterViewModel(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
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
                observeCategoriesUseCase()
                    .map {
                        it.mapAsync(categoryFilterMapper::invoke)
                    }
                    .onEach { _loading.value = false }
                    .collect(_categories::setValue)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }
}