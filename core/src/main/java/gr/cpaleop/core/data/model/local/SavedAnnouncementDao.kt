package gr.cpaleop.core.data.model.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SavedAnnouncementDao {

    @Query("SELECT * FROM savedannouncement")
    fun getAll(): List<SavedAnnouncement>

    /*@Query("SELECT * FROM savedannouncement")
    fun getAllFlow(): Flow<List<SavedAnnouncement>>

    @Query("SELECT * FROM savedannouncement WHERE announcementId = (:announcementId)")
    fun getById(announcementId: String): SavedAnnouncement?*/
}