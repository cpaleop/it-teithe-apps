package gr.cpaleop.dashboard.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteNotificationDetails(
    @SerialName("_id")
    val id: String,
    @SerialName("_notification")
    val remoteNotification: RemoteNotification?,
    val seen: Boolean?
)

@Serializable
data class RemoteNotification(
    val date: String,
    val nameEl: String,
    val nameEn: String,
    val related: RemoteRelated
)

@Serializable
data class RemoteRelated(
    val id: RemoteRelatedAnnouncement
)

@Serializable
data class RemoteRelatedAnnouncement(
    @SerialName("_about")
    val about: RemoteRelatedAnnouncementAbout,
    @SerialName("_id")
    val id: String,
    val title: String,
    val titleEn: String
)

@Serializable
data class RemoteRelatedAnnouncementAbout(
    val name: String
)