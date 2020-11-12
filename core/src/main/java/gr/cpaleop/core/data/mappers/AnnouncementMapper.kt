package gr.cpaleop.core.data.mappers

import gr.cpaleop.core.datasource.model.response.RemoteAnnouncement
import gr.cpaleop.core.datasource.model.response.RemoteCategory
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category

class AnnouncementMapper {

    operator fun invoke(remoteAnnouncement: RemoteAnnouncement, remoteCategory: RemoteCategory?): Announcement {
        val category = Category(
            id = remoteCategory?.id ?: "",
            name = remoteCategory?.name ?: "",
            isRegistered = false
        )

        return Announcement(
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