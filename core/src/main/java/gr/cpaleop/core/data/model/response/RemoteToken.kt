package gr.cpaleop.core.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteToken(
    @SerialName("access_token")
    val accessToken: String? = "",
    @SerialName("refresh_token")
    val refreshToken: String? = ""
)