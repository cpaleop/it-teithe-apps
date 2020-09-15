package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilterAnnouncementsUseCaseImpl : FilterAnnouncementsUseCase {

    override suspend fun invoke(
        announcements: List<Announcement>,
        query: String
    ): List<Announcement> = withContext(Dispatchers.Default) {
        if (query.isEmpty()) return@withContext announcements

        announcements.filter { announcement ->
            announcement.title.contains(query, true) ||
                    announcement.text.contains(query, true) ||
                    announcement.publisherName.contains(query, true) ||
                    announcement.date.contains(query, true)
        }
    }
}