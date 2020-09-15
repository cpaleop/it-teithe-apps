package gr.cpaleop.categoryfilter.presentation

import androidx.lifecycle.*
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.usecases.GetAnnouncementsByCategoryUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val announcements: MediatorLiveData<List<Announcement>> by lazy {
        MediatorLiveData<List<Announcement>>().apply {
            addSource(_announcements) {
                this.value = it
            }
        }
    }

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

    fun filterAnnouncements(query: String) {
        viewModelScope.launch {
            announcements.value = withContext(Dispatchers.Default) {
                _announcements.value?.filter { announcement ->
                    announcement.title.contains(query, true) ||
                            announcement.text.contains(query, true) ||
                            announcement.publisherName.contains(query, true) ||
                            announcement.date.contains(query, true)
                }
            }
        }
    }
}