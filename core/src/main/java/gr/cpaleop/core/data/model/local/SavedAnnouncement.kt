package gr.cpaleop.core.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedAnnouncement(
    @PrimaryKey
    val announcementId: String,
    val dateAdded: Long
)