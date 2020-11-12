package gr.cpaleop.core.datasource.model.local

import androidx.room.*
import gr.cpaleop.core.datasource.model.response.RemoteAnnouncement
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RemoteAnnouncementsDao {

    suspend fun nukeAndInsertAll(remoteAnnouncementList: List<RemoteAnnouncement>) {
        nuke()
        insertAll(remoteAnnouncementList)
    }

    @Transaction
    @Query("DELETE from remoteannouncement")
    abstract suspend fun nuke()

    @Query("SELECT * FROM remoteannouncement")
    abstract suspend fun fetchAll(): List<RemoteAnnouncement>

    @Query("SELECT * FROM remoteannouncement")
    abstract fun observeAll(): Flow<List<RemoteAnnouncement>>

    @Query("SELECT * FROM remoteannouncement WHERE about = (:categoryId)")
    abstract fun observeByCategoryId(categoryId: String): Flow<List<RemoteAnnouncement>>

    @Query("SELECT * FROM remoteannouncement WHERE id= :id")
    abstract suspend fun fetchFromId(id: String): List<RemoteAnnouncement>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(remoteAnnouncementList: List<RemoteAnnouncement>)
}