package gr.cpaleop.announcements.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteAnnouncementTitleFilter(
    val titleEn: String? = null
)