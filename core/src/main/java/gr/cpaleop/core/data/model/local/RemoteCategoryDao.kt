package gr.cpaleop.core.data.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gr.cpaleop.core.data.model.response.RemoteCategory

@Dao
interface RemoteCategoryDao {

    @Query("SELECT * FROM remotecategory")
    suspend fun fetchAll(): List<RemoteCategory>

    @Query("SELECT * FROM remotecategory WHERE id = :id")
    suspend fun fetchFromId(id: String?): RemoteCategory

    @Query("SELECT * FROM remotecategory WHERE id = :id")
    suspend fun fetchAllFromId(id: String?): List<RemoteCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteCategorytList: List<RemoteCategory>)
}