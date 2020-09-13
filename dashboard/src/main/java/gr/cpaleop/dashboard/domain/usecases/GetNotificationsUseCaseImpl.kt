package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository

class GetNotificationsUseCaseImpl(
    private val notificationsRepository: NotificationsRepository,
    private val dateFormatter: DateFormatter
) : GetNotificationsUseCase {

    override suspend fun invoke(): List<Notification> {
        return notificationsRepository.getNotifications()
            .sortedByDescending { dateFormatter(it.announcement.date) }
    }
}