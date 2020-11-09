package gr.cpaleop.create_announcement.domain.repositories

import gr.cpaleop.create_announcement.domain.entities.Attachment

interface AttachmentsRepository {

    suspend fun clearSelections(uriList: List<String>)

    suspend fun addAttachments(uriList: List<String>)

    fun getAttachments(): List<Attachment>
}