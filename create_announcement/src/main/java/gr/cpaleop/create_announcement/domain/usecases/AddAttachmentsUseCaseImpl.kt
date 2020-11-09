package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.create_announcement.domain.repositories.AttachmentsRepository

class AddAttachmentsUseCaseImpl(private val attachmentsRepository: AttachmentsRepository) :
    AddAttachmentsUseCase {

    override suspend fun invoke(attachmentUriList: List<String>) {
        return attachmentsRepository.addAttachments(attachmentUriList)
    }
}