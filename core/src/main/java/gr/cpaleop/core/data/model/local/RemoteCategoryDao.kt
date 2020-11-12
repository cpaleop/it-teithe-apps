package gr.cpaleop.core.data.model.local

import androidx.room.*
import gr.cpaleop.core.data.model.response.RemoteCategory
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RemoteCategoryDao {

    suspend fun nukeAndInsertAll(remoteCategorytList: List<RemoteCategory>) {
        nuke()
        insertAll(remoteCategorytList)
    }

    @Transaction
    @Query("DELETE from remotecategory")
    abstract suspend fun nuke()

    @Query("SELECT * FROM remotecategory")
    abstract suspend fun fetchAll(): List<RemoteCategory>

    @Query("SELECT * FROM remotecategory")
    abstract fun fetchAllFlow(): Flow<List<RemoteCategory>>

    @Query("SELECT * FROM remotecategory WHERE id = :id")
    abstract suspend fun fetchFromId(id: String?): RemoteCategory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(remoteCategorytList: List<RemoteCategory>)
}