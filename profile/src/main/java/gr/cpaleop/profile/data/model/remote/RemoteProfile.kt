package gr.cpaleop.profile.data.model.remote

import com.google.gson.annotations.SerializedName

data class RemoteProfile(
    @SerializedName("uid")
    val username: String?,
    val am: String?,
    val cn: String?,
    @SerializedName("cn;lang-el")
    val cnEl: String?,
    val description: String?,
    @SerializedName("description;lang-el")
    val descriptionEl: String?,
    val displayName: String?,
    @SerializedName("displayName;lang-el")
    val displayNameEl: String?,
    val eduPersonAffiliation: String?,
    val eduPersonScopedAffiliation: String?,
    val eduPersonEntitlement: String?,
    val eduPersonPrimaryAffiliation: String?,
    val fathersname: String?,
    @SerializedName("fathersname;lang-el")
    val fathersnameEl: String?,
    val givenName: String?,
    @SerializedName("givenName;lang-el")
    val givenNameEl: String?,
    val labeledURI: String?,
    val mail: String?,
    val pwdChangedTime: String?,
    val regsem: String?,
    val regyear: String?,
    val secondarymail: String?,
    val sem: String?,
    val sn: String?,
    @SerializedName("sn;lang-el")
    val snEl: String?,
    val telephoneNumber: String?,
    val title: String?,
    @SerializedName("title;lang-el")
    val titleEl: String?,
    val profilePhoto: String?,
    val socialMedia: RemoteSocialMedia?
)