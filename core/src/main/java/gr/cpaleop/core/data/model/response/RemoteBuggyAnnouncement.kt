package gr.cpaleop.core.data.model.response

import com.google.gson.annotations.SerializedName

data class RemoteBuggyAnnouncement(
    @SerializedName("_about")
    val about: RemoteBuggyAbout,
    @SerializedName("_id")
    val id: String,
    val attachments: List<String>,
    val date: String,
    val publisher: RemotePublisher,
    val text: String,
    val textEn: String,
    val title: String,
    val titleEn: String
)

data class RemoteBuggyAbout(
    @SerializedName("_id")
    val id: String
)