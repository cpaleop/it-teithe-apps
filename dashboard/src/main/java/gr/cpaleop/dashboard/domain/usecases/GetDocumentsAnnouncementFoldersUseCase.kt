package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder

interface GetDocumentsAnnouncementFoldersUseCase {

    suspend operator fun invoke(): List<AnnouncementFolder>
}