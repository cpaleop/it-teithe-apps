package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.core.domain.entities.DocumentSortType
import gr.cpaleop.documents.domain.FilterStream
import gr.cpaleop.documents.domain.entities.AnnouncementFolder
import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class ObserveDocumentsAnnouncementFoldersUseCaseImpl(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val deviceStorageRepository: DeviceStorageRepository,
    private val observeDocumentSortUseCase: ObserveDocumentSortUseCase,
    private val filterStream: FilterStream
) : ObserveDocumentsAnnouncementFoldersUseCase {

    private val titleSelector: (AnnouncementFolder) -> String =
        { announcementFolder ->
            announcementFolder.title
        }

    private val lastModifiedSelector: (AnnouncementFolder) -> Long =
        { announcementFolder ->
            announcementFolder.lastModified
        }

    private val distinctSelector: (AnnouncementFolder) -> String =
        { announcementFolder ->
            announcementFolder.id
        }

    override suspend fun invoke(): Flow<List<AnnouncementFolder>> =
        withContext(defaultDispatcher) {
            val announcementFoldersFlow = deviceStorageRepository.getAnnouncementFoldersFlow()

            return@withContext announcementFoldersFlow
                .combine(observeDocumentSortUseCase(), ::sortAnnouncementFolderList)
                .combine(filterStream, ::filterAnnouncementFolderList)
        }

    private fun filterAnnouncementFolderList(
        announcementFolderList: List<AnnouncementFolder>,
        query: String
    ): List<AnnouncementFolder> {
        return announcementFolderList.filter { announcementFolder ->
            if (query.isEmpty()) return@filter true
            announcementFolder.title.contains(query, true)
        }
    }

    private fun sortAnnouncementFolderList(
        announcementFolderList: List<AnnouncementFolder>,
        documentSort: DocumentSort
    ): List<AnnouncementFolder> {
        return when (documentSort.type) {
            DocumentSortType.DATE -> {
                if (documentSort.descending) {
                    announcementFolderList.sortedByDescending(lastModifiedSelector)
                } else {
                    announcementFolderList.sortedBy(lastModifiedSelector)
                }
            }
            DocumentSortType.ALPHABETICAL -> {
                if (documentSort.descending) {
                    announcementFolderList.sortedByDescending(titleSelector)
                } else {
                    announcementFolderList.sortedBy(titleSelector)
                }
            }
            else -> throw IllegalArgumentException("No sorting type found with the value ${documentSort.type}")
        }.distinctBy(distinctSelector)
    }
}