package gr.cpaleop.categoryfilter.presentation

import androidx.lifecycle.*
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

@FlowPreview
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

    val announcements: LiveData<List<AnnouncementPresentation>> by lazy {
        try {
            observeAnnouncementsByCategoryUseCase(categoryId)
                .map { it.mapAsync { announcement -> announcementPresentationMapper(announcement) } }
                .flowOn(defaultDispatcher)
                .asLiveData(mainDispatcher)
        } catch (t: Throwable) {
            Timber.e(t)
            handleNoConnectionException(t)
            MutableLiveData()
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
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }

    fun presentAnnouncements() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                observeAnnouncementsByCategoryUseCase.refresh(categoryId)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun filterAnnouncements(query: String) {
        viewModelScope.launch(mainDispatcher) {
            observeAnnouncementsByCategoryUseCase.filter(query)
        }
    }
}