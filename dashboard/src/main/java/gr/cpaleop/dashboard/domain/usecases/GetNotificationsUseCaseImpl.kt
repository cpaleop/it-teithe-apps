package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository

class GetNotificationsUseCaseImpl(private val notificationsRepository: NotificationsRepository) :
    GetNotificationsUseCase {

    override suspend fun invoke(): List<Notification> {
        return notificationsRepository.getNotifications()
    }
}