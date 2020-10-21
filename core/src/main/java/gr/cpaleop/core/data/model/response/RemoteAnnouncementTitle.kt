package gr.cpaleop.core.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteAnnouncementTitle(
    @SerialName("_id")
    val id: String? = null,
    val title: String? = null,
    val titleEn: String? = null
)