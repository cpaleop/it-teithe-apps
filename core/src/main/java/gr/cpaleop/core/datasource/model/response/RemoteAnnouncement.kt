package gr.cpaleop.core.datasource.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class RemoteAnnouncement(
    @PrimaryKey
    @SerialName("_id")
    val id: String,
    @SerialName("_about")
    val about: String? = null,
    val titleEn: String? = null,
    val title: String? = null,
    val textEn: String? = null,
    val text: String? = null,
    val publisher: RemotePublisher? = null,
    val date: String? = null,
    val attachments: List<String>? = emptyList()
)