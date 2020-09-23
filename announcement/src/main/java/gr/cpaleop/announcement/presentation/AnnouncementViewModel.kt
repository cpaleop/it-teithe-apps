package gr.cpaleop.announcement.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementViewModel(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val getAnnouncementUseCase: GetAnnouncementUseCase,
    private val announcementDetailsMapper: AnnouncementDetailsMapper
) : ViewModel() {

    private val _announcement = MutableLiveData<AnnouncementDetails>()
    val announcement: LiveData<AnnouncementDetails> = _announcement.toSingleEvent()

    private val _attachmentFileId = MutableLiveData<AnnouncementDocument>()
    val attachmentFileId: LiveData<AnnouncementDocument> = _attachmentFileId.toSingleEvent()

    fun presentAnnouncement(id: String) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                _announcement.value = announcementDetailsMapper(getAnnouncementUseCase(id))
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun downloadAttachments(id: String) {
        viewModelScope.launch(defaultDispatcher) {
            try {
                val mAnnouncement = getAnnouncementUseCase(id)
                _attachmentFileId.value =
                    AnnouncementDocument(mAnnouncement.id, mAnnouncement.attachments)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}