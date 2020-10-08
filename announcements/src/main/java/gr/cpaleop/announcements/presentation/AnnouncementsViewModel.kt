package gr.cpaleop.announcements.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import gr.cpaleop.announcements.domain.usecases.FilterAnnouncementsUseCase
import gr.cpaleop.announcements.domain.usecases.ObserveAnnouncementsUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.teithe_apps.R
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
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
) : BaseViewModel() {

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
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(R.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
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