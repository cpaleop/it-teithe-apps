package gr.cpaleop.core.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RemotePublisher(
    val id: String? = "",
    val name: String? = ""
)