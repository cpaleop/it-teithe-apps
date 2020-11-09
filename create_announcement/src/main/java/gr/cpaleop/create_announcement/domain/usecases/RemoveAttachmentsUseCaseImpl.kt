package gr.cpaleop.create_announcement.domain.usecases

import gr.cpaleop.create_announcement.domain.repositories.AttachmentsRepository

class RemoveAttachmentsUseCaseImpl(private val attachmentsRepository: AttachmentsRepository) :
    RemoveAttachmentsUseCase {

    override suspend fun invoke(uriList: List<String>) {
        attachmentsRepository.clearSelections(uriList)
    }
}