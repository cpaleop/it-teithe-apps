package gr.cpaleop.dashboard.data.mappers

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnnouncementMapper {

    suspend operator fun invoke(
        remoteAnnouncement: RemoteAnnouncement,
        remoteCategories: List<RemoteCategory>
    ): Announcement = withContext(Dispatchers.Default) {
        val remoteCategory = remoteCategories.firstOrNull { it.id == remoteAnnouncement.about }
        val category = Category(
            id = remoteCategory?.id ?: "",
            name = remoteCategory?.name ?: ""
        )
        Announcement(
            id = remoteAnnouncement.id,
            title = remoteAnnouncement.title ?: remoteAnnouncement.titleEn ?: "",
            date = remoteAnnouncement.date ?: "",
            text = remoteAnnouncement.textEn ?: "",
            category = category,
            publisherName = remoteAnnouncement.publisher?.name ?: "",
            attachments = remoteAnnouncement.attachments ?: emptyList()
        )
    }
}