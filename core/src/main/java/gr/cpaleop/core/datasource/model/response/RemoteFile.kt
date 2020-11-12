package gr.cpaleop.core.datasource.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteFile(
    @SerialName("_id")
    val id: String?,
    val name: String?,
    val contentType: String?,
    val data: RemoteData?
)

@Serializable
data class RemoteData(
    val type: String?,
    val data: List<Int>
)