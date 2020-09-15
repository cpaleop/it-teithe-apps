package gr.cpaleop.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteAnnouncementsDao {

    @Query("SELECT * FROM remoteannouncement")
    suspend fun getAll(): List<RemoteAnnouncement>

    @Query("SELECT * FROM remoteannouncement")
    fun observeAll(): Flow<List<RemoteAnnouncement>>

    @Query("SELECT * FROM remoteannouncement WHERE about = (:categoryId)")
    fun observeCategory(categoryId: String): Flow<List<RemoteAnnouncement>>

    @Query("SELECT * FROM remoteannouncement WHERE id= :id")
    suspend fun getFromId(id: String): List<RemoteAnnouncement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteAnnouncementList: List<RemoteAnnouncement>)
}