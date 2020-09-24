package gr.cpaleop.announcement.domain.usecases

import gr.cpaleop.download.domain.DownloadAnnouncementNotifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class ObserveDownloadNotifierUseCaseImpl(private val downloadAnnouncementNotifier: DownloadAnnouncementNotifier) :
    ObserveDownloadNotifierUseCase {

    override suspend fun invoke(announcementId: String): Flow<Boolean> {
        return downloadAnnouncementNotifier.asFlow()
            .filter { it.announcementId == announcementId }
            .map { it.downloading }
    }
}