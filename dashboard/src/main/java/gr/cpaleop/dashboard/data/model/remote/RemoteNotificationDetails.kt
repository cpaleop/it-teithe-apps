package gr.cpaleop.dashboard.data.model.remote

import com.google.gson.annotations.SerializedName

data class RemoteNotificationDetails(
    @SerializedName("_id")
    val id: String,
    @SerializedName("_notification")
    val remoteNotification: RemoteNotification?,
    val seen: Boolean?
)

data class RemoteNotification(
    val date: String,
    val nameEl: String,
    val nameEn: String,
    val related: RemoteRelated
)

data class RemoteRelated(
    val id: RemoteRelatedAnnouncement
)

data class RemoteRelatedAnnouncement(
    @SerializedName("_about")
    val about: RemoteRelatedAnnouncementAbout,
    @SerializedName("_id")
    val id: String,
    val title: String,
    val titleEn: String
)

data class RemoteRelatedAnnouncementAbout(
    val name: String
)