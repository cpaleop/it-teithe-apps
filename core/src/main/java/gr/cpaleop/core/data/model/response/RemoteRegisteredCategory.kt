package gr.cpaleop.core.data.model.response

import com.google.gson.annotations.SerializedName

data class RemoteRegisteredCategory(
    @SerializedName("_id")
    val id: String,
    val registered: List<String>
)