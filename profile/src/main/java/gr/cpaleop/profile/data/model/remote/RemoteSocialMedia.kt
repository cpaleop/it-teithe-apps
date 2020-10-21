package gr.cpaleop.profile.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class RemoteSocialMedia(
    val facebook: String?,
    val twitter: String?,
    val github: String?,
    val googlePlus: String?,
    val linkedIn: String?
)