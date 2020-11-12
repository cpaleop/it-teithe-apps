package gr.cpaleop.core.datasource.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementCategoryFilter(
    @SerialName("_about")
    val about: String
)