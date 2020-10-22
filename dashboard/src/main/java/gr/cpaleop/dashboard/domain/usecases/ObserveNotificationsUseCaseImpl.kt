package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.common.extensions.removeIntonation
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class ObserveNotificationsUseCaseImpl(private val notificationsRepository: NotificationsRepository) :
    ObserveNotificationsUseCase {

    private val _filterStream = MutableStateFlow("")
    override val filterStream: StateFlow<String>
        get() = _filterStream

    override suspend fun invoke(): Flow<List<Notification>> {
        _filterStream.value = ""
        return flow {
            emit(notificationsRepository.getNotifications()
                .sortedByDescending { it.announcement.date })
        }.combine(filterStream) { notifications, filter ->
            if (filter.isEmpty()) return@combine notifications
            notifications.filter { notification ->
                notification.announcement.title.removeIntonation()
                    .contains(filter.removeIntonation(), true) ||
                        notification.announcement.publisherName.removeIntonation()
                            .contains(filter.removeIntonation(), true)
            }
        }
    }

    override fun filter(filterQuery: String) {
        _filterStream.value = filterQuery
    }
}