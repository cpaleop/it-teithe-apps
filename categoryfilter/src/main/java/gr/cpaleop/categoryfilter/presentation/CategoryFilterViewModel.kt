package gr.cpaleop.categoryfilter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.usecases.GetAnnouncementsByCategoryUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class CategoryFilterViewModel(
    private val getCategoryNameUseCase: GetCategoryNameUseCase,
    private val getAnnouncementsByCategoryUseCase: GetAnnouncementsByCategoryUseCase
) : ViewModel() {

    var categoryId: String = ""

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName.toSingleEvent()

    private val _announcements = MutableLiveData<List<Announcement>>()
    val announcements: LiveData<List<Announcement>> = _announcements.toSingleEvent()

    fun presentCategoryName() {
        viewModelScope.launch {
            try {
                _categoryName.value = getCategoryNameUseCase(categoryId)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentAnnouncementsByCategory() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _announcements.value = getAnnouncementsByCategoryUseCase(categoryId)
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }
}