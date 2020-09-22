package gr.cpaleop.dashboard.domain.usecases

interface FilterAnnouncementsUseCase {

    suspend operator fun invoke(filter: String)
}