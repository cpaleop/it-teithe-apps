package gr.cpaleop.announcement.data

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteCategory
import gr.cpaleop.core.domain.entities.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnnouncementMapper {

    suspend operator fun invoke(
        remoteAnnouncement: RemoteAnnouncement,
        remoteCategory: RemoteCategory
    ): Announcement = withContext(Dispatchers.Default) {
        Announcement(
            id = remoteAnnouncement.id ?: "",
            title = remoteAnnouncement.title ?: remoteAnnouncement.titleEn ?: "",
            date = remoteAnnouncement.date ?: "",
            text = remoteAnnouncement.textEn ?: "",
            category = remoteCategory.name ?: "",
            publisherName = remoteAnnouncement.publisher?.name ?: ""
        )
    }
}