package gr.cpaleop.dashboard.presentation.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.ObserveAnnouncementsUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementsViewModel(
    private val observeAnnouncementsUseCase: ObserveAnnouncementsUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper
) : ViewModel() {

    private val _announcements = MutableLiveData<PagingData<AnnouncementPresentation>>()
    val announcements: LiveData<PagingData<AnnouncementPresentation>> =
        _announcements.toSingleEvent()

    fun presentAnnouncements() {
        viewModelScope.launch {
            try {
                observeAnnouncementsUseCase().collect {
                    _announcements.value = it.map { announcement ->
                        announcementPresentationMapper(announcement)
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}