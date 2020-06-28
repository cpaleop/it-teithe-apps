package gr.cpaleop.announcement.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.launch
import timber.log.Timber

class AnnouncementViewModel(private val getAnnouncementUseCase: GetAnnouncementUseCase) :
    ViewModel() {

    private val _announcement = MutableLiveData<Announcement>()
    val announcement: LiveData<Announcement> = _announcement.toSingleEvent()

    fun presentAnnouncement(id: String) {
        viewModelScope.launch {
            try {
                _announcement.value = getAnnouncementUseCase(id)
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }
}