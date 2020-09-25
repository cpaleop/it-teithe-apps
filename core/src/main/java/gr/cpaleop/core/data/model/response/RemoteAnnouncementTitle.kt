package gr.cpaleop.core.data.model.response

import com.google.gson.annotations.SerializedName

data class RemoteAnnouncementTitle(
    @SerializedName("_id")
    val id: String? = null,
    val title: String? = null,
    val titleEn: String? = null
)