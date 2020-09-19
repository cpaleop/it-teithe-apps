package gr.cpaleop.dashboard.domain.usecases

interface RenameFileUseCase {

    suspend operator fun invoke(fileUri: String, newName: String)
}