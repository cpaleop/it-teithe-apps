package gr.cpaleop.core.data.model.response

import com.google.gson.annotations.SerializedName

data class RemoteFile(
    @SerializedName("_id")
    val id: String?,
    val name: String?,
    val contentType: String?,
    val data: RemoteData?,
    @SerializedName("_announcement")
    val announcement: Any?
)

data class RemoteData(
    val type: String?,
    val data: List<Byte>
)