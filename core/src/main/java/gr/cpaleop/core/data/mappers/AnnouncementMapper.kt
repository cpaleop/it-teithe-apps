package gr.cpaleop.core.data.mappers

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnnouncementMapper {

    suspend operator fun invoke(
        remoteAnnouncement: RemoteAnnouncement,
        remoteCategory: RemoteCategory?
    ): Announcement = withContext(Dispatchers.Default) {
        val category = Category(
            id = remoteCategory?.id ?: "",
            name = remoteCategory?.name ?: "",
            isRegistered = false
        )
        Announcement(
            id = remoteAnnouncement.id,
            title = remoteAnnouncement.title ?: remoteAnnouncement.titleEn ?: "",
            date = remoteAnnouncement.date ?: "",
            text = remoteAnnouncement.textEn ?: remoteAnnouncement.text ?: "",
            category = category,
            publisherName = remoteAnnouncement.publisher?.name ?: "",
            attachments = remoteAnnouncement.attachments ?: emptyList()
        )
    }
}