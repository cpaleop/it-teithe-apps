package gr.cpaleop.core.data.model.response

import com.google.gson.annotations.SerializedName

data class RemoteToken(
    @SerializedName("access_token")
    val accessToken: String? = "",
    @SerializedName("refresh_token")
    val refreshToken: String? = ""
)