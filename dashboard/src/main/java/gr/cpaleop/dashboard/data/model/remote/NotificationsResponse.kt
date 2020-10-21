package gr.cpaleop.dashboard.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class NotificationsResponse(
    val notifications: List<RemoteNotificationDetails>
)