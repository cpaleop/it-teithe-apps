package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetDocumentsAnnouncementFoldersUseCaseImpl(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val deviceStorageRepository: DeviceStorageRepository,
    private val preferencesRepository: PreferencesRepository
) : GetDocumentsAnnouncementFoldersUseCase {

    override suspend fun invoke(): List<AnnouncementFolder> = withContext(defaultDispatcher) {
        val documentSort = preferencesRepository.getDocumentSort()
        val announcementFolders = deviceStorageRepository.getAnnouncementFolders()

        var sortedAnnouncementFolders = emptyList<AnnouncementFolder>()
        when (documentSort.type) {
            DocumentSortType.DATE -> {
                sortedAnnouncementFolders = if (documentSort.descending) {
                    announcementFolders.sortedByDescending {
                        it.lastModified
                    }
                } else {
                    announcementFolders.sortedBy {
                        it.lastModified
                    }
                }
            }
            DocumentSortType.ALPHABETICAL -> {
                sortedAnnouncementFolders = if (documentSort.descending) {
                    announcementFolders.sortedByDescending {
                        it.title
                    }
                } else {
                    announcementFolders.sortedBy {
                        it.title
                    }
                }
            }
        }

        return@withContext sortedAnnouncementFolders.distinctBy { it.id }
    }
}