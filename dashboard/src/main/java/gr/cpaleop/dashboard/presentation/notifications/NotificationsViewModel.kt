package gr.cpaleop.dashboard.presentation.notifications

import androidx.lifecycle.*
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.ReadAllNotificationsUseCase
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
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
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading.toSingleEvent()

    private val _notifications = MutableLiveData<List<Notification>>()

    val notificationsCounter: MediatorLiveData<Int> by lazy {
        MediatorLiveData<Int>().apply {
            addSource(_notifications) { notifications ->
                this.value = notifications.count { !it.seen }
            }
        }
    }

    val notifications: MediatorLiveData<List<Notification>> by lazy {
        MediatorLiveData<List<Notification>>().apply {
            addSource(_notifications) {
                this@apply.value = it
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
                _notifications.value = getNotificationsUseCase()
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
                    it.announcement.title.contains(query, true) ||
                            it.announcement.category.contains(query, true) ||
                            it.announcement.date.contains(query, true)
                } ?: emptyList()
            }
        }
    }
}