package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.create_announcement.domain.entities.Attachment
import gr.cpaleop.create_announcement.domain.repositories.AttachmentsRepository

class GetSelectedAttachmentsUseCaseImpl(private val attachmentsRepository: AttachmentsRepository) :
    GetSelectedAttachmentsUseCase {

    override fun invoke(): List<Attachment> {
        return attachmentsRepository.getAttachments()
    }
}