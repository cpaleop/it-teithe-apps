package gr.cpaleop.dashboard.presentation.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.FilterAnnouncementsUseCase
import gr.cpaleop.dashboard.domain.usecases.ObserveAnnouncementsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementsViewModel(
    private val observeAnnouncementsUseCase: ObserveAnnouncementsUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper,
    private val filterAnnouncementsUseCase: FilterAnnouncementsUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _announcements = MutableLiveData<PagingData<AnnouncementPresentation>>()
    val announcements: LiveData<PagingData<AnnouncementPresentation>> =
        _announcements.toSingleEvent()

    fun presentAnnouncements() {
        viewModelScope.launch {
            try {
                observeAnnouncementsUseCase()
                    .cachedIn(viewModelScope)
                    .collect {
                        _announcements.value = it.map(announcementPresentationMapper::invoke)
                        /*_announcements.value = it.filter { announcement ->
                            announcement.publisherName.contains(searchQuery, true) ||
                                    announcement.title.contains(searchQuery, true) ||
                                    announcement.text.contains(searchQuery, true) ||
                                    announcement.date.contains(searchQuery, true)
                        }.map { announcement ->
                            announcementPresentationMapper(announcement)
                        }*/
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
                /*searchAnnouncementUseCase(query)*/
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }
}