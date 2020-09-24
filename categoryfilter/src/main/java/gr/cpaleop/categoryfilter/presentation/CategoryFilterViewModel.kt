package gr.cpaleop.categoryfilter.presentation

import androidx.lifecycle.*
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.usecases.FilterAnnouncementsUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class CategoryFilterViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val getCategoryNameUseCase: GetCategoryNameUseCase,
    private val observeAnnouncementsByCategoryUseCase: ObserveAnnouncementsByCategoryUseCase,
    private val filterAnnouncementsUseCase: FilterAnnouncementsUseCase
) : ViewModel() {

    var categoryId: String = ""

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName.toSingleEvent()

    private val announcementsFilterChannel = ConflatedBroadcastChannel("")

    private val _announcements = MutableLiveData<List<Announcement>>()
    val announcements: LiveData<List<Announcement>> = _announcements.toSingleEvent()

    val announcementsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(announcements) {
                this.value = it.isEmpty()
            }
        }
    }

    fun presentCategoryName() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _categoryName.value = getCategoryNameUseCase(categoryId)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentAnnouncementsByCategory() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                val filterFlow = announcementsFilterChannel
                    .asFlow()
                    .debounce(200)
                    .onEach { _loading.value = true }

                observeAnnouncementsByCategoryUseCase(categoryId)
                    .combine(filterFlow) { announcements, query ->
                        filterAnnouncementsUseCase(announcements, query)
                    }
                    .flowOn(mainDispatcher)
                    .collect {
                        _loading.value = false
                        _announcements.value = it
                    }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun filterAnnouncements(query: String) {
        viewModelScope.launch(mainDispatcher) {
            announcementsFilterChannel.send(query)
        }
    }
}