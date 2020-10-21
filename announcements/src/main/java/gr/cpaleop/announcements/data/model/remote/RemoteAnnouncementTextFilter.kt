package gr.cpaleop.announcements.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteAnnouncementTextFilter(
    val textEn: String? = null
)