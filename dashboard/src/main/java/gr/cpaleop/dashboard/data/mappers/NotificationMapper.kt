package gr.cpaleop.dashboard.data.mappers

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.dashboard.data.model.remote.RemoteNotificationDetails
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.entities.NotificationRelatedAnnouncement
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class NotificationMapper(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val dateFormatter: DateFormatter
) {

    suspend operator fun invoke(
        remoteNotificationDetails: RemoteNotificationDetails
    ): Notification =
        withContext(defaultDispatcher) {

            val date = remoteNotificationDetails.remoteNotification?.date ?: ""
            val relatedAnnouncement = NotificationRelatedAnnouncement(
                id = remoteNotificationDetails.remoteNotification?.related?.id?.id ?: "",
                date = dateFormatter(date),
                title = remoteNotificationDetails.remoteNotification?.related?.id?.title
                    ?: remoteNotificationDetails.remoteNotification?.related?.id?.titleEn
                    ?: "",
                category = remoteNotificationDetails.remoteNotification?.related?.id?.about?.name
                    ?: "",
                publisherName = remoteNotificationDetails.remoteNotification?.nameEl
                    ?: remoteNotificationDetails.remoteNotification?.nameEn ?: ""
            )

            Notification(
                id = remoteNotificationDetails.id,
                seen = remoteNotificationDetails.seen ?: false,
                announcement = relatedAnnouncement
            )
        }
}