package gr.cpaleop.dashboard.presentation.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.entities.AnnouncementSortType
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.announcements.options.AnnouncementSortOption
import gr.cpaleop.dashboard.presentation.announcements.options.AnnouncementSortOptionMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AnnouncementsViewModel(
    private val observeAnnouncementsUseCase: ObserveAnnouncementsUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper,
    private val filterAnnouncementsUseCase: FilterAnnouncementsUseCase,
    private val getAnnouncementSortOptionUseCase: GetAnnouncementSortOptionUseCase,
    private val updateAnnouncementsSortUseCase: UpdateAnnouncementsSortUseCase,
    private val getAnnouncementsSortOptionsUseCase: GetAnnouncementsSortOptionsUseCase,
    private val announcementSortOptionMapper: AnnouncementSortOptionMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _dismissSortOptionsDialog = MutableLiveData<Unit>()
    val dismissSortOptionsDialog: LiveData<Unit> = _dismissSortOptionsDialog.toSingleEvent()

    private val _announcements = MutableLiveData<PagingData<AnnouncementPresentation>>()
    val announcements: LiveData<PagingData<AnnouncementPresentation>> =
        _announcements.toSingleEvent()

    private val _announcementSort = MutableLiveData<AnnouncementSortOption>()
    val announcementSort: LiveData<AnnouncementSortOption> = _announcementSort.toSingleEvent()

    private val _options = MutableLiveData<List<AnnouncementSortOption>>()
    val options: LiveData<List<AnnouncementSortOption>> = _options.toSingleEvent()

    fun presentAnnouncements() {
        viewModelScope.launch {
            try {
                observeAnnouncementsUseCase(viewModelScope)
                    .cachedIn(viewModelScope)
                    .collect {
                        _announcements.value = it.map(announcementPresentationMapper::invoke)
                    }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun searchAnnouncements(filterQuery: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                filterAnnouncementsUseCase(filterQuery)
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun presentAnnouncementsSortSelected() {
        viewModelScope.launch {
            try {
                _announcementSort.value =
                    announcementSortOptionMapper(getAnnouncementSortOptionUseCase())
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentSortOptions() {
        viewModelScope.launch {
            _options.value = withContext(Dispatchers.Default) {
                getAnnouncementsSortOptionsUseCase().mapAsync(announcementSortOptionMapper::invoke)
            }
        }
    }

    fun updateAnnouncementSort(
        @AnnouncementSortType type: Int,
        descending: Boolean,
        selected: Boolean
    ) {
        viewModelScope.launch {
            try {
                val descend = if (!selected) descending else !descending
                _announcementSort.value =
                    announcementSortOptionMapper(
                        updateAnnouncementsSortUseCase(
                            AnnouncementSort(
                                type,
                                true,
                                descend
                            )
                        )
                    )
                _dismissSortOptionsDialog.value = Unit
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}