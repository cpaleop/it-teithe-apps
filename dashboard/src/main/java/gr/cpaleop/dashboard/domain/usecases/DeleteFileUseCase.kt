package gr.cpaleop.dashboard.domain.usecases

interface DeleteFileUseCase {

    suspend operator fun invoke(fileUri: String)
}