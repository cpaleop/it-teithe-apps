package gr.cpaleop.core.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RemoteCategory(
    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    val name: String? = null,
    val nameEn: String? = null
)