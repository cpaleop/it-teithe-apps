package gr.cpaleop.announcement.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementViewModel(
    private val getAnnouncementUseCase: GetAnnouncementUseCase,
    private val announcementDetailsMapper: AnnouncementDetailsMapper
) : ViewModel() {

    private val _announcement = MutableLiveData<AnnouncementDetails>()
    val announcement: LiveData<AnnouncementDetails> = _announcement.toSingleEvent()

    private val _attachmentFileId = MutableLiveData<String>()
    val attachmentFileId: LiveData<String> = _attachmentFileId.toSingleEvent()

    fun presentAnnouncement(id: String) {
        viewModelScope.launch {
            try {
                _announcement.value = announcementDetailsMapper(getAnnouncementUseCase(id))
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun downloadAttachments() {
        viewModelScope.launch {
            try {
                _attachmentFileId.value =
                    announcement.value?.attachments?.firstOrNull() ?: return@launch
            } catch (t: Throwable) {

            }
        }
    }
}