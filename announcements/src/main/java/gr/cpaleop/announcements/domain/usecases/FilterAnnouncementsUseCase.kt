package gr.cpaleop.announcements.domain.usecases

interface FilterAnnouncementsUseCase {

    suspend operator fun invoke(filter: String)
}