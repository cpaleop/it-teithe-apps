package gr.cpaleop.dashboard.data.mappers

import gr.cpaleop.dashboard.data.model.remote.RemoteNotificationDetails
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.entities.NotificationRelatedAnnouncement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationMapper {

    suspend operator fun invoke(
        remoteNotificationDetails: RemoteNotificationDetails
    ): Notification =
        withContext(Dispatchers.Default) {
            val relatedAnnouncement = NotificationRelatedAnnouncement(
                id = remoteNotificationDetails.remoteNotification?.related?.id?.id ?: "",
                date = remoteNotificationDetails.remoteNotification?.date ?: "",
                title = remoteNotificationDetails.remoteNotification?.related?.id?.title
                    ?: remoteNotificationDetails.remoteNotification?.related?.id?.titleEn
                    ?: "",
                category = remoteNotificationDetails.remoteNotification?.related?.id?.about?.name
                    ?: ""
            )

            Notification(
                id = remoteNotificationDetails.id,
                seen = remoteNotificationDetails.seen ?: false/* new*/,
                announcement = relatedAnnouncement
            )
        }
}