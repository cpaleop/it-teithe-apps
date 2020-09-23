package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.dashboard.domain.entities.Notification

interface NotificationsRepository {

    suspend fun getNotifications(): List<Notification>

    suspend fun readAllNotifications()
}