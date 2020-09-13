package gr.cpaleop.categoryfilter.data.mappers

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.core.data.model.response.RemoteAnnouncement

class AnnouncementMapper {

    operator fun invoke(remoteAnnouncement: RemoteAnnouncement): Announcement {
        return Announcement(
            id = remoteAnnouncement.id,
            attachments = remoteAnnouncement.attachments ?: emptyList(),
            publisherName = remoteAnnouncement.publisher?.name ?: "",
            text = remoteAnnouncement.textEn ?: "",
            date = remoteAnnouncement.date ?: "",
            title = remoteAnnouncement.title ?: ""
        )
    }
}