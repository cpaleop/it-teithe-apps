package gr.cpaleop.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.favorites.domain.usecases.ObserveFavoriteAnnouncementsUseCase
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class FavoritesViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val announcementPresentationMapper: AnnouncementPresentationMapper,
    private val observeFavoriteAnnouncementsUseCase: ObserveFavoriteAnnouncementsUseCase
) : BaseViewModel() {

    private val _announcements = MutableLiveData<List<AnnouncementPresentation>>()
    val announcements: LiveData<List<AnnouncementPresentation>> = _announcements.toSingleEvent()

    private val _announcementsEmpty = MutableLiveData<Boolean>()
    val announcementsEmpty: LiveData<Boolean> = _announcementsEmpty.toSingleEvent()

    fun presentAnnouncements() {
        viewModelScope.launch(mainDispatcher) {
            try {
                observeFavoriteAnnouncementsUseCase()
                    .map { announcementList ->
                        announcementList.mapAsync { announcement ->
                            announcementPresentationMapper(
                                announcement,
                                observeFavoriteAnnouncementsUseCase.filter
                            )
                        }
                    }
                    .flowOn(defaultDispatcher)
                    .onEach { _announcementsEmpty.value = it.isEmpty() }
                    .collect(_announcements::setValue)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun filter(query: String) {
        observeFavoriteAnnouncementsUseCase.filter = query
    }
}