package gr.cpaleop.core.data.datasources

import gr.cpaleop.core.datasource.model.response.RemoteAnnouncement
import gr.cpaleop.core.domain.entities.SavedAnnouncement
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AnnouncementsDataSource {

    suspend fun fetchSavedAnnouncementsFlow(): Flow<List<RemoteAnnouncement>>

    suspend fun fetchSavedAnnouncementByIdFlow(id: String): Flow<SavedAnnouncement?>

    suspend fun fetchSavedAnnouncementById(id: String): SavedAnnouncement?

    suspend fun saveAnnouncement(savedAnnouncement: SavedAnnouncement)

    suspend fun removeSavedAnnouncement(savedAnnouncement: SavedAnnouncement)

    suspend fun fetchPublicAnnouncements(): List<RemoteAnnouncement>

    suspend fun fetchAnnouncementById(id: String): RemoteAnnouncement

    suspend fun updateCachedAnnouncementsByCategoryId(categoryId: String)

    suspend fun getCachedAnnouncementsByCategoryIdFlow(categoryId: String): Flow<List<RemoteAnnouncement>>

    suspend fun createAnnouncement(
        files: List<MultipartBody.Part?>?,
        title: RequestBody,
        titleEn: RequestBody,
        text: RequestBody,
        textEn: RequestBody,
        about: RequestBody
    )
}