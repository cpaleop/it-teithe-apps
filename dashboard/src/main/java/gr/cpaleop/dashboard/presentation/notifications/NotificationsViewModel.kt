package gr.cpaleop.dashboard.presentation.notifications

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.ReadAllNotificationsUseCase
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
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

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

    fun presentNotifications() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                _notifications.value =
                    getNotificationsUseCase().mapAsync(notificationPresentationMapper::invoke)
            } catch (t: Throwable) {
                Timber.e(t)
            } finally {
                _loading.value = false
            }
        }
    }

    fun readAllNotifications() {
        viewModelScope.launch(mainDispatcher) {
            try {
                readAllNotificationsUseCase()
            } catch (t: Throwable) {
                Timber.e(t)
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