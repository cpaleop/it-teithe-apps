package gr.cpaleop.dashboard.data

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.dashboard.data.mappers.NotificationMapper
import gr.cpaleop.dashboard.data.remote.NotificationsApi
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.repositories.NotificationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationsRepositoryImpl(
    private val notificationsApi: NotificationsApi,
    private val notificationMapper: NotificationMapper
) : NotificationsRepository {

    override suspend fun getNotifications(): List<Notification> = withContext(Dispatchers.IO) {
        val remoteNotifications = notificationsApi.fetchNotifications()
        remoteNotifications.notifications.mapAsyncSuspended(notificationMapper::invoke)
    }

    override suspend fun readAllNotifications() = withContext(Dispatchers.IO) {
        notificationsApi.readAllNotifications()
    }
}