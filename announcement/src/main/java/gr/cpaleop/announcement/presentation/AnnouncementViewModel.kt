package gr.cpaleop.announcement.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.announcement.domain.usecases.ObserveDownloadNotifierUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.teithe_apps.R
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val getAnnouncementUseCase: GetAnnouncementUseCase,
    private val announcementDetailsMapper: AnnouncementDetailsMapper,
    private val observeDownloadNotifierUseCase: ObserveDownloadNotifierUseCase
) : BaseViewModel() {

    private val _announcement = MutableLiveData<AnnouncementDetails>()
    val announcement: LiveData<AnnouncementDetails> = _announcement.toSingleEvent()

    val downloadStatus: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(announcement) { announcementDetails ->
                viewModelScope.launch(mainDispatcher) {
                    try {
                        observeDownloadNotifierUseCase(announcementDetails.id)
                            .flowOn(mainDispatcher)
                            .collect {
                                this@apply.value = it
                            }
                    } catch (t: NoConnectionException) {
                        Timber.e(t)
                        _message.value = Message(R.string.error_no_internet_connection)
                    } catch (t: Throwable) {
                        Timber.e(t)
                        _message.value = Message(R.string.error_generic)
                    }
                }
            }
        }
    }

    private val _attachmentFileId = MutableLiveData<AnnouncementDocument>()
    val attachmentFileId: LiveData<AnnouncementDocument> = _attachmentFileId.toSingleEvent()

    fun presentAnnouncement(id: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                _announcement.value = announcementDetailsMapper(getAnnouncementUseCase(id))
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(R.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            }
        }
    }

    fun downloadAttachments(id: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                val mAnnouncement = getAnnouncementUseCase(id)
                _attachmentFileId.value =
                    AnnouncementDocument(mAnnouncement.id, mAnnouncement.attachments)
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(R.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(R.string.error_generic)
            }
        }
    }
}