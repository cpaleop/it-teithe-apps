package gr.cpaleop.core.data.model.local

import androidx.room.*
import gr.cpaleop.core.domain.entities.Document
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {

    @Query("SELECT * FROM document")
    fun fetchAll(): Flow<List<Document>>

    @Query("SELECT * FROM document WHERE announcementId = :announcementId")
    fun fetchByAnnouncementId(announcementId: String): Flow<List<Document>>

    @Query("SELECT * FROM document WHERE uri = :uri")
    suspend fun fetchByUri(uri: String): Document

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(document: Document)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(documentList: List<Document>)

    @Delete
    suspend fun delete(document: Document)

    @Delete
    suspend fun deleteAll(documentList: List<Document>)
}