package gr.cpaleop.core.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Document(
    @PrimaryKey
    val uri: String,
    val announcementId: String,
    val absolutePath: String,
    val name: String,
    val type: String,
    val size: Long,
    val previewUri: String,
    val lastModified: Long
)