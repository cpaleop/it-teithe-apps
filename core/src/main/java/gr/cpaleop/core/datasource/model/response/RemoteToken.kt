package gr.cpaleop.core.datasource.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteToken(
    @SerialName("access_token")
    val accessToken: String? = "",
    @SerialName("refresh_token")
    val refreshToken: String? = ""
)