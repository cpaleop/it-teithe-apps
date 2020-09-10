package gr.cpaleop.dashboard.presentation.notifications

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class NotificationsViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val notificationPresentationMapper: NotificationPresentationMapper
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _notifications = MutableLiveData<List<NotificationPresentation>>()

    val notifications: MediatorLiveData<List<NotificationPresentation>> by lazy {
        MediatorLiveData<List<NotificationPresentation>>().apply {
            addSource(_notifications) {
                this.value = it
            }
        }
    }

    val notificationsEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(_notifications) {
                this.value = it.isEmpty()
            }
        }
    }

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

    fun searchNotifications(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                notifications.postValue(_notifications.value?.filter {
                    it.announcement.title.contains(query, true) ||
                            it.announcement.category.contains(query, true) ||
                            it.announcement.date.contains(query, true)
                })
            }
        }
    }
}