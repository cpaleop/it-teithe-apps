package gr.cpaleop.public_announcements.presentation

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.public_announcements.domain.usecases.ObservePublicAnnouncementsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class PublicAnnouncementsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val observePublicAnnouncementsUseCase: ObservePublicAnnouncementsUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper
) : ViewModel() {

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
                observePublicAnnouncementsUseCase()
                    .map { it.mapAsync(announcementPresentationMapper::invoke) }
                    .flowOn(defaultDispatcher)
                    .onStart { _loading.value = true }
                    .onEach { _loading.value = false }
                    .collect(_announcements::setValue)
            } catch (t: Throwable) {
                Timber.e(t)
                _loading.value = false
            }
        }
    }
}