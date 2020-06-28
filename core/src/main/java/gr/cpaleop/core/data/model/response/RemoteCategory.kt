package gr.cpaleop.core.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RemoteCategory(
    @PrimaryKey
    @SerializedName("_id")
    var id: String,
    var name: String?,
    var nameEn: String?/*,
    var public: Boolean*/
)