package gr.cpaleop.core.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RemoteAnnouncement(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @SerializedName("_about")
    val about: String? = "",
    val titleEn: String? = "",
    val title: String? = "",
    val textEn: String? = "",
    val publisher: RemotePublisher?,
    val date: String? = "",
    val attachments: List<String>? = emptyList()
)