package gr.cpaleop.core.datasource.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteRegisteredCategory(
    @SerialName("_id")
    val id: String,
    val registered: List<String>
)