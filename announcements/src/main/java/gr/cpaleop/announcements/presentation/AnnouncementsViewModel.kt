package gr.cpaleop.announcements.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import gr.cpaleop.announcements.domain.usecases.FilterAnnouncementsUseCase
import gr.cpaleop.announcements.domain.usecases.ObserveAnnouncementsUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val observeAnnouncementsUseCase: ObserveAnnouncementsUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper,
    private val filterAnnouncementsUseCase: FilterAnnouncementsUseCase
) : ViewModel() {

    private val _announcements = MutableLiveData<PagingData<AnnouncementPresentation>>()
    val announcements: LiveData<PagingData<AnnouncementPresentation>> =
        _announcements.toSingleEvent()

    fun presentAnnouncements() {
        viewModelScope.launch(mainDispatcher) {
            try {
                observeAnnouncementsUseCase()
                    .flowOn(mainDispatcher)
                    .collect {
                        _announcements.value = it.mapSync(announcementPresentationMapper::invoke)
                    }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun searchAnnouncements(filterQuery: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                filterAnnouncementsUseCase(filterQuery)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}