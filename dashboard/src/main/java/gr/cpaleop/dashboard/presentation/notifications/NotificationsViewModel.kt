package gr.cpaleop.dashboard.presentation.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val notificationPresentationMapper: NotificationPresentationMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _notifications = MutableLiveData<List<NotificationPresentation>>()
    val notifications: LiveData<List<NotificationPresentation>> = _notifications.toSingleEvent()

    fun presentNotifications() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _notifications.value =
                    getNotificationsUseCase().mapAsyncSuspended(notificationPresentationMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }
}