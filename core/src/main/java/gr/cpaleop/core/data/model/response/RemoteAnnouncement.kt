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
    val about: String? = null,
    val titleEn: String? = null,
    val title: String? = null,
    val textEn: String? = null,
    val publisher: RemotePublisher? = null,
    val date: String? = null,
    val attachments: List<String>? = emptyList()
)