package gr.cpaleop.core.data.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteAnnouncementsDao {

    @Query("SELECT * FROM remoteannouncement")
    suspend fun fetchAll(): List<RemoteAnnouncement>

    @Query("SELECT * FROM remoteannouncement")
    fun observeAll(): Flow<List<RemoteAnnouncement>>

    @Query("SELECT * FROM remoteannouncement WHERE about = (:categoryId)")
    fun observeByCategoryId(categoryId: String): Flow<List<RemoteAnnouncement>>

    @Query("SELECT * FROM remoteannouncement WHERE id= :id")
    suspend fun fetchFromId(id: String): List<RemoteAnnouncement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteAnnouncementList: List<RemoteAnnouncement>)
}