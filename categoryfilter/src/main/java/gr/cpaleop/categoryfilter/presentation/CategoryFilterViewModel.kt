package gr.cpaleop.categoryfilter.presentation

import androidx.lifecycle.*
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common.extensions.mapAsync
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

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

    val announcements: LiveData<List<AnnouncementPresentation>> by lazy {
        try {
            observeAnnouncementsByCategoryUseCase(categoryId)
                .map { it.mapAsync { announcement -> announcementPresentationMapper(announcement) } }
                .flowOn(defaultDispatcher)
                .asLiveData(mainDispatcher)
        } catch (t: NoConnectionException) {
            Timber.e(t)
            _message.value = Message(R.string.error_no_internet_connection)
            return@lazy MutableLiveData()
        } catch (t: Throwable) {
            Timber.e(t)
            _message.value = Message(R.string.error_generic)
            return@lazy MutableLiveData()
        }
    }

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
                observeAnnouncementsByCategoryUseCase.filter(query)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            }
        }
    }
}