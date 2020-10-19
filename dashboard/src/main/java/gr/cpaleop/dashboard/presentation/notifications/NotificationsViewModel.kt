package gr.cpaleop.dashboard.presentation.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gr.cpaleop.common.extensions.mapAsync
import gr.cpaleop.common.extensions.toSingleEvent
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.dashboard.domain.usecases.ObserveNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.ReadAllNotificationsUseCase
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.teithe_apps.presentation.base.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
class NotificationsViewModel(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val observeNotificationsUseCase: ObserveNotificationsUseCase,
    private val readAllNotificationsUseCase: ReadAllNotificationsUseCase,
    private val notificationPresentationMapper: NotificationPresentationMapper
) : BaseViewModel() {

    private var observeNotificationsJob: Job? = null

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
            addSource(notifications) {
                this.value = it.isEmpty()
            }
        }
    }

    fun showMessage(message: Message) {
        _message.value = message
    }

    fun presentNotifications() {
        observeNotificationsJob?.cancel()
        observeNotificationsJob = viewModelScope.launch(mainDispatcher) {
            try {
                _loading.value = true
                observeNotificationsUseCase()
                    .map { notificationList ->
                        notificationList.mapAsync {
                            notificationPresentationMapper(
                                it,
                                observeNotificationsUseCase.filterStream.value
                            )
                        }
                    }.flowOn(defaultDispatcher)
                    .onEach { _loading.value = false }
                    .collect(_notifications::setValue)
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(appR.string.error_no_internet_connection)
            } catch (t: CancellationException) {
                Timber.e(t)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            } finally {
                _loading.value = false
            }
        }
    }

    fun readAllNotifications() {
        viewModelScope.launch(mainDispatcher) {
            try {
                _readNotifications.value = readAllNotificationsUseCase()
            } catch (t: NoConnectionException) {
                Timber.e(t)
                _message.value = Message(appR.string.error_no_internet_connection)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }

    fun searchNotifications(query: String) {
        viewModelScope.launch(mainDispatcher) {
            try {
                observeNotificationsUseCase.filter(query)
            } catch (t: Throwable) {
                Timber.e(t)
                _message.value = Message(appR.string.error_generic)
            }
        }
    }
}