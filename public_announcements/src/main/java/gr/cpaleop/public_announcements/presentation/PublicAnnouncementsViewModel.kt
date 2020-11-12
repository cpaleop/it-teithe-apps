package gr.cpaleop.public_announcements.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.public_announcements.domain.usecases.ObservePublicAnnouncementsUseCase
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
class PublicAnnouncementsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val observePublicAnnouncementsUseCase: ObservePublicAnnouncementsUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper
) : BaseViewModel() {

    private var observeAnnouncementsJob: Job? = null

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _announcements = MutableLiveData<List<AnnouncementPresentation>>()
    val announcements: LiveData<List<AnnouncementPresentation>> = _announcements.toSingleEvent()

    val announcementsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(announcements) {
                this.value = it.isEmpty()
            }
        }
    }

    fun presentAnnouncements() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                observeAnnouncementsJob?.cancel()
                observeAnnouncementsJob = observeAnnouncements()
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(appR.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }

    private fun observeAnnouncements(): Job {
        return viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                observePublicAnnouncementsUseCase()
                    .map { it.map { announcementPresentationMapper(it, observePublicAnnouncementsUseCase.filter) } }
                    .flowOn(defaultDispatcher)
                    .onStart { _loading.value = true }
                    .onEach { _loading.value = false }
                    .collect {
                        Timber.e("COLLECTING")
                        _announcements.value = it
                    }
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(appR.string.error_no_internet_connection)
            } catch (t: CancellationException) {
                Timber.e(t)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }

    fun search(query: String) {
        observePublicAnnouncementsUseCase.filter = query
    }
}