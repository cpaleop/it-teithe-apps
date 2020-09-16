package gr.cpaleop.dashboard.domain.usecases

interface GetFileOptionsUseCase {

    operator fun invoke(): List<String>
}