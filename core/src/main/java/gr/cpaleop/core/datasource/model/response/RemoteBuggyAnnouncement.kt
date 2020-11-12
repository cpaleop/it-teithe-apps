package gr.cpaleop.core.datasource.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteBuggyAnnouncement(
    @SerialName("_about")
    val about: RemoteBuggyAbout,
    @SerialName("_id")
    val id: String,
    val attachments: List<String>,
    val date: String,
    val publisher: RemotePublisher,
    val text: String,
    val textEn: String,
    val title: String,
    val titleEn: String
)

@Serializable
data class RemoteBuggyAbout(
    @SerialName("_id")
    val id: String
)