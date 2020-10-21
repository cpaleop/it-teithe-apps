package gr.cpaleop.profile.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProfile(
    @SerialName("uid")
    val username: String?,
    val am: String?,
    val cn: String?,
    @SerialName("cn;lang-el")
    val cnEl: String?,
    val description: String?,
    @SerialName("description;lang-el")
    val descriptionEl: String?,
    val displayName: String?,
    @SerialName("displayName;lang-el")
    val displayNameEl: String?,
    val eduPersonAffiliation: String?,
    val fathersname: String?,
    @SerialName("fathersname;lang-el")
    val fathersnameEl: String?,
    val givenName: String?,
    @SerialName("givenName;lang-el")
    val givenNameEl: String?,
    val labeledURI: String?,
    val mail: String?,
    val pwdChangedTime: String?,
    val regsem: String?,
    val regyear: String?,
    val secondarymail: String?,
    val sem: String?,
    val sn: String?,
    @SerialName("sn;lang-el")
    val snEl: String?,
    val telephoneNumber: String?,
    val title: String?,
    @SerialName("title;lang-el")
    val titleEl: String?,
    val profilePhoto: String?,
    val socialMedia: RemoteSocialMedia?
)