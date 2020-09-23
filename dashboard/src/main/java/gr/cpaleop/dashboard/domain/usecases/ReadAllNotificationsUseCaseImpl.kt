package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository

class ReadAllNotificationsUseCaseImpl(private val notificationsRepository: NotificationsRepository) :
    ReadAllNotificationsUseCase {

    override suspend fun invoke() {
        notificationsRepository.readAllNotifications()
    }
}