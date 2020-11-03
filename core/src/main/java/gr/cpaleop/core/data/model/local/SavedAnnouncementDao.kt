package gr.cpaleop.core.data.model.local

import androidx.room.*
import gr.cpaleop.core.domain.entities.SavedAnnouncement
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedAnnouncementDao {

    @Query("SELECT * FROM savedannouncement")
    fun fetchAll(): List<SavedAnnouncement>

    @Query("SELECT * FROM savedannouncement WHERE announcementId = (:announcementId)")
    suspend fun fetchById(announcementId: String): SavedAnnouncement?

    @Query("SELECT * FROM savedannouncement")
    fun fetchAllAsFlow(): Flow<List<SavedAnnouncement>>

    @Query("SELECT * FROM savedannouncement WHERE announcementId = (:announcementId)")
    fun fetchByIdAsFlow(announcementId: String): Flow<SavedAnnouncement?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(savedAnnouncement: SavedAnnouncement)

    @Delete
    suspend fun remove(savedAnnouncement: SavedAnnouncement)
}