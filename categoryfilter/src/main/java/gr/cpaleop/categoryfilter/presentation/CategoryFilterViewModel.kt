package gr.cpaleop.categoryfilter.presentation

import androidx.lifecycle.*
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.usecases.FilterAnnouncementsUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class CategoryFilterViewModel(
    private val getCategoryNameUseCase: GetCategoryNameUseCase,
    private val observeAnnouncementsByCategoryUseCase: ObserveAnnouncementsByCategoryUseCase,
    private val filterAnnouncementsUseCase: FilterAnnouncementsUseCase
) : ViewModel() {

    var categoryId: String = ""

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName.toSingleEvent()

    private val announcementsFilterChannel = ConflatedBroadcastChannel<String>("")

    private val _announcements = MutableLiveData<List<Announcement>>()

    val announcements: MediatorLiveData<List<Announcement>> by lazy {
        MediatorLiveData<List<Announcement>>().apply {
            addSource(_announcements) {
                this.value = it
            }
        }
    }

    val announcementsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(announcements) {
                this.value = it.isEmpty()
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
                observeAnnouncementsByCategoryUseCase(categoryId)
                    .combine(
                        announcementsFilterChannel
                            .asFlow()
                            .debounce(200)
                    ) { announcements, query ->
                        filterAnnouncementsUseCase(announcements, query)
                    }
                    .collect {
                        _loading.value = false
                        _announcements.value = it
                    }
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun filterAnnouncements(query: String) {
        _loading.value = true
        announcementsFilterChannel.offer(query)
    }
}