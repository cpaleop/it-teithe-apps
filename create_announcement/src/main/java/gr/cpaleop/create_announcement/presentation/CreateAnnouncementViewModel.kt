package gr.cpaleop.create_announcement.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.create_announcement.R
import gr.cpaleop.create_announcement.domain.entities.*
import gr.cpaleop.create_announcement.domain.usecases.*
import gr.cpaleop.create_announcement.presentation.attachments.AttachmentPresentation
import gr.cpaleop.create_announcement.presentation.attachments.AttachmentPresentationMapper
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

class CreateAnnouncementViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val createAnnouncementUseCase: CreateAnnouncementUseCase,
    private val getSelectedAttachmentsUseCase: GetSelectedAttachmentsUseCase,
    private val addAttachmentsUseCase: AddAttachmentsUseCase,
    private val removeAttachmentsUseCase: RemoveAttachmentsUseCase,
    private val attachmentPresentationMapper: AttachmentPresentationMapper
) : BaseViewModel() {

    // The announcement that will be submitted
    private var newAnnouncement = NewAnnouncement(
        MultilanguageText("", ""),
        MultilanguageText("", ""),
        "",
        emptyList()
    )

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories.toSingleEvent()

    private val _category = MutableLiveData<Category>()
    val category: LiveData<Category> = _category.toSingleEvent()

    private val _attachments = MutableLiveData<List<Attachment>>()
    val attachments: LiveData<List<AttachmentPresentation>> by lazy {
        MediatorLiveData<List<AttachmentPresentation>>().apply {
            addSource(_attachments) {
                newAnnouncement = newAnnouncement.copy(
                    attachments = it
                )
                viewModelScope.launch(mainDispatcher) {
                    this@apply.value = it.mapAsync(attachmentPresentationMapper::invoke)
                }
            }
        }
    }

    val attachmentsEmpty: LiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(attachments) {
                this.value = it.isEmpty()
            }
        }
    }

    val attachmentsCounterVisibility: LiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(attachments) {
                this.value = it.isNotEmpty()
            }
        }
    }

    val attachmentsCounter: LiveData<String> by lazy {
        MediatorLiveData<String>().apply {
            addSource(attachments) {
                this.value = if (it.size > 10) "9+"
                else it.size.toString()
            }
        }
    }

    private val _categorySelected = MutableLiveData<Unit>()
    val categorySelected: LiveData<Unit> = _categorySelected.toSingleEvent()

    private val _announcementCreated = MutableLiveData<Unit>()
    val announcementCreated: LiveData<Unit> = _announcementCreated.toSingleEvent()

    fun addTitleEn(titleEn: String) {
        newAnnouncement = newAnnouncement.copy(title = newAnnouncement.title.copy(en = titleEn))
    }

    fun addTitleGr(titleGr: String) {
        newAnnouncement = newAnnouncement.copy(title = newAnnouncement.title.copy(gr = titleGr))
    }

    fun addTextEn(textEn: String) {
        newAnnouncement = newAnnouncement.copy(text = newAnnouncement.text.copy(en = textEn))
    }

    fun addTextGr(textGr: String) {
        newAnnouncement = newAnnouncement.copy(text = newAnnouncement.text.copy(gr = textGr))
    }

    fun addCategory(category: String) {
        newAnnouncement = newAnnouncement.copy(category = category)
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

    fun addAttachments(attachmentUriList: List<String>) {
        viewModelScope.launch(mainDispatcher) {
            try {
                addAttachmentsUseCase(attachmentUriList)
                _attachments.value = getSelectedAttachmentsUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun removeAttachment(uri: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                removeAttachmentsUseCase(listOf(uri))
                _attachments.value = getSelectedAttachmentsUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun presentAttachments() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _attachments.value = getSelectedAttachmentsUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}