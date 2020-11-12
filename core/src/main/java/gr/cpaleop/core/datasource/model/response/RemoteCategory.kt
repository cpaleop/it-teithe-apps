package gr.cpaleop.core.datasource.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class RemoteCategory(
    @PrimaryKey
    @SerialName("_id")
    val id: String,
    val name: String? = null,
    val nameEn: String? = null
)