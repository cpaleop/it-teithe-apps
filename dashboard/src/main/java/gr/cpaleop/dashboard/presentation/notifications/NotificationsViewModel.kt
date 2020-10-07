package gr.cpaleop.dashboard.presentation.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.SnackbarMessage
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.ReadAllNotificationsUseCase
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class NotificationsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase,
    private val notificationPresentationMapper: NotificationPresentationMapper
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _readNotifications = MutableLiveData<Unit>()

    private val _notifications = MutableLiveData<List<NotificationPresentation>>()

    val notificationsCounter: MediatorLiveData<Int> by lazy {
        MediatorLiveData<Int>().apply {
            addSource(_notifications) { notifications ->
                viewModelScope.launch(mainDispatcher) {
                    this@apply.value = withContext(defaultDispatcher) {
                        notifications.count { !it.seen }
                    }
                }
            }
            addSource(_readNotifications) {
                this.value = 0
            }
        }
    }

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

    val notificationsFilterEmpty: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(notifications) {
                this.value = it.isEmpty()
            }
        }
    }

    fun showMessage(snackbarMessage: SnackbarMessage) {
        _message.value = snackbarMessage
    }

    fun presentNotifications() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                _notifications.value =
                    getNotificationsUseCase().mapAsync(notificationPresentationMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun readAllNotifications() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _readNotifications.value = readAllNotificationsUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
                handleNoConnectionException(t)
            }
        }
    }

    fun searchNotifications(query: String) {
        viewModelScope.launch(mainDispatcher) {
            notifications.value = withContext(defaultDispatcher) {
                _notifications.value?.filter {
                    it.title.contains(query, true) ||
                            it.category.contains(query, true) ||
                            it.date.contains(query, true)
                } ?: emptyList()
            }
        }
    }
}