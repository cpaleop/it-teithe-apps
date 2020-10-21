package gr.cpaleop.categoryfilter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCategoryFilter(
    @SerialName("_id")
    val id: String
)