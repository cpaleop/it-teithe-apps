package gr.cpaleop.dashboard.domain.usecases

class GetFileOptionsUseCaseImpl : GetFileOptionsUseCase {

    override fun invoke(): List<String> {
        return listOf("Rename", "Delete", "Share", "Info", "Go to announcement")
    }
}