package gr.cpaleop.create_announcement.domain.usecases

interface AddAttachmentsUseCase {

    suspend operator fun invoke(attachmentUriList: List<String>)
}