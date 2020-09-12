package gr.cpaleop.dashboard.presentation.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.ObserveAnnouncementsUseCase
import gr.cpaleop.dashboard.domain.usecases.SearchAnnouncementUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementsViewModel(
    private val observeAnnouncementsUseCase: ObserveAnnouncementsUseCase,
    private val searchAnnouncementUseCase: SearchAnnouncementUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _announcements = MutableLiveData<PagingData<AnnouncementPresentation>>()
    val announcements: LiveData<PagingData<AnnouncementPresentation>> =
        _announcements.toSingleEvent()

    private var searchQuery: String = ""

    fun presentAnnouncements() {
        viewModelScope.launch {
            try {
                observeAnnouncementsUseCase()
                    .collect {
                        _announcements.value = it.filter { announcement ->
                            announcement.publisherName.contains(searchQuery, false) ||
                                    announcement.title.contains(searchQuery, false) ||
                                    announcement.text.contains(searchQuery, false) ||
                                    announcement.date.contains(searchQuery, false)
                        }.map { announcement ->
                            announcementPresentationMapper(announcement)
                        }
                    }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun searchAnnouncements(query: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                searchQuery = query
                /*searchAnnouncementUseCase(query)*/
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }
}