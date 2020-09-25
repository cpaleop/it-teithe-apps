package gr.cpaleop.core.data.model.local

import androidx.room.*
import gr.cpaleop.core.domain.entities.Document

@Dao
interface DocumentDao {

    @Query("SELECT * FROM document")
    suspend fun fetchAll(): List<Document>

    @Query("SELECT * FROM document WHERE announcementId = :announcementId")
    suspend fun fetchByAnnouncementId(announcementId: String): List<Document>

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