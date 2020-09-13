package gr.cpaleop.categoryfilter.data.model

import com.google.gson.annotations.SerializedName

data class AnnouncementCategoryFilter(
    @SerializedName("_about")
    val about: String
)