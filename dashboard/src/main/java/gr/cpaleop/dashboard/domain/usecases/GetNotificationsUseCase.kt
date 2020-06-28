package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.Notification

interface GetNotificationsUseCase {

    suspend operator fun invoke(): List<Notification>
}