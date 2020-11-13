package gr.cpaleop.categoryfilter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.teithe_apps.R
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
class CategoryFilterViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val getCategoryNameUseCase: GetCategoryNameUseCase,
    private val observeAnnouncementsByCategoryUseCase: ObserveAnnouncementsByCategoryUseCase,
    private val announcementPresentationMapper: AnnouncementPresentationMapper
) : BaseViewModel() {

    var categoryId: String = ""

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName.toSingleEvent()

    private val _announcements = MutableLiveData<List<AnnouncementPresentation>>()
    val announcements: LiveData<List<AnnouncementPresentation>> = _announcements.toSingleEvent()

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
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(R.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            }
        }
    }

    fun presentAnnouncements() {
        viewModelScope.launch(mainDispatcher) {
            try {
                observeAnnouncementsByCategoryUseCase(categoryId)
                    .map {
                        it.map { announcement ->
                            announcementPresentationMapper(
                                announcement,
                                observeAnnouncementsByCategoryUseCase.filter
                            )
                        }
                    }
                    .flowOn(defaultDispatcher)
                    .flowOn(mainDispatcher)
                    .collect(_announcements::setValue)
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(R.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            }
        }
    }

    fun refreshAnnouncements() {
        viewModelScope.launch {
            try {
                _loading.value = true
                observeAnnouncementsByCategoryUseCase.refresh(categoryId)
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(R.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }

    fun filterAnnouncements(query: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                observeAnnouncementsByCategoryUseCase.filter = query
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            }
        }
    }
}