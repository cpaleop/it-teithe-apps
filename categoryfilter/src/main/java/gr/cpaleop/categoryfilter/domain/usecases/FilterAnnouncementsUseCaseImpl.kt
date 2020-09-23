package gr.cpaleop.categoryfilter.domain.usecases

import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FilterAnnouncementsUseCaseImpl(@DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher) :
    FilterAnnouncementsUseCase {

    override suspend fun invoke(
        announcements: List<Announcement>,
        query: String
    ): List<Announcement> = withContext(defaultDispatcher) {
        if (query.isEmpty()) return@withContext announcements

        announcements.filter { announcement ->
            announcement.title.contains(query, true) ||
                    announcement.text.contains(query, true) ||
                    announcement.publisherName.contains(query, true) ||
                    announcement.date.contains(query, true)
        }
    }
}