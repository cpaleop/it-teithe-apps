package gr.cpaleop.upload.domain.repositories

import gr.cpaleop.upload.domain.entities.NewAnnouncement

interface AnnouncementsRepository {

    suspend fun createAnnouncement(newAnnouncement: NewAnnouncement)
}