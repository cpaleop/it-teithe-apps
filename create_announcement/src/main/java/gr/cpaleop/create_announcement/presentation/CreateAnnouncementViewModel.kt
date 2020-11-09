package gr.cpaleop.create_announcement.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.create_announcement.R
import gr.cpaleop.create_announcement.domain.entities.*
import gr.cpaleop.create_announcement.domain.usecases.CreateAnnouncementUseCase
import gr.cpaleop.create_announcement.domain.usecases.GetCategoriesUseCase
import gr.cpaleop.create_announcement.domain.usecases.GetCategoryUseCase
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

class CreateAnnouncementViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val createAnnouncementUseCase: CreateAnnouncementUseCase
) : BaseViewModel() {

    // The announcement that will be submitted
    private var newAnnouncement = NewAnnouncement(
        MultilanguageText("", ""),
        MultilanguageText("", ""),
        ""
    )

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories.toSingleEvent()

    private val _category = MutableLiveData<Category>()
    val category: LiveData<Category> = _category.toSingleEvent()

    private val _categorySelected = MutableLiveData<Unit>()
    val categorySelected: LiveData<Unit> = _categorySelected.toSingleEvent()

    private val _announcementCreated = MutableLiveData<Unit>()
    val announcementCreated: LiveData<Unit> = _announcementCreated.toSingleEvent()

    // Observe input values and update the entity
    fun updateAnnouncementValues(
        titleEn: String = "", titleGr: String = "",
        textEn: String = "", textGr: String = "",
        category: String = ""
    ) {
        newAnnouncement = newAnnouncement.copy(
            title = MultilanguageText(titleEn, titleGr),
            text = MultilanguageText(textEn, textGr),
            category = category
        )
    }

    fun createAnnouncement() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _announcementCreated.value = createAnnouncementUseCase(newAnnouncement)
            } catch (t: EmptyTitleException) {
                Timber.e(t)
                _message.value = Message(R.string.create_announcement_error_title_empty)
            } catch (t: EmptyTextException) {
                Timber.e(t)
                _message.value = Message(R.string.create_announcement_error_text_empty)
            } catch (t: EmptyCategoryException) {
                Timber.e(t)
                _message.value = Message(R.string.create_announcement_error_category_empty)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentCategories() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _categories.value = getCategoriesUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun selectCategory(id: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _category.value = getCategoryUseCase(id)
                newAnnouncement = newAnnouncement.copy(category = id)
                _categorySelected.value = Unit
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }
}