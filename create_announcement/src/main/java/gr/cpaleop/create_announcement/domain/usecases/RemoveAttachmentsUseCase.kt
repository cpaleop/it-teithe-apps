package gr.cpaleop.create_announcement.domain.usecases

interface RemoveAttachmentsUseCase {

    suspend operator fun invoke(uriList: List<String> = emptyList())
}