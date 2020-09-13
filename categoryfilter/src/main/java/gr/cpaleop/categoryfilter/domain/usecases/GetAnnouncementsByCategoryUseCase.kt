package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement

interface GetAnnouncementsByCategoryUseCase {

    suspend operator fun invoke(categoryId: String): List<Announcement>
}