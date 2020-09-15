package gr.cpaleop.teithe_apps.data

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import gr.cpaleop.core.data.model.response.RemoteBuggyAnnouncement

class RemoteAnnouncementMapper {

    operator fun invoke(remoteBuggyAnnouncement: RemoteBuggyAnnouncement): RemoteAnnouncement {
        return RemoteAnnouncement(
            id = remoteBuggyAnnouncement.id,
            date = remoteBuggyAnnouncement.date,
            title = remoteBuggyAnnouncement.title,
            about = remoteBuggyAnnouncement.about.id,
            attachments = remoteBuggyAnnouncement.attachments,
            publisher = remoteBuggyAnnouncement.publisher,
            textEn = remoteBuggyAnnouncement.textEn,
            titleEn = remoteBuggyAnnouncement.titleEn
        )
    }
}