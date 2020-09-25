package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

@FlowPreview
@ExperimentalCoroutinesApi
class ObserveDocumentsAnnouncementFoldersUseCaseImpl(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val deviceStorageRepository: DeviceStorageRepository,
    private val dateFormatter: DateFormatter,
    private val observeDocumentSortUseCase: ObserveDocumentSortUseCase
) : ObserveDocumentsAnnouncementFoldersUseCase {

    private val filterChannel = ConflatedBroadcastChannel("")

    private val titleSelector: (AnnouncementFolder) -> String = { announcementFolder ->
        announcementFolder.title
    }

    private val lastModifiedSelector: (AnnouncementFolder) -> Long = { announcementFolder ->
        announcementFolder.lastModified
    }

    private val distinctSelector: (AnnouncementFolder) -> String = { announcementFolder ->
        announcementFolder.id
    }

    override suspend fun invoke(): Flow<List<AnnouncementFolder>> = withContext(defaultDispatcher) {
        val announcementFoldersFlow = deviceStorageRepository.getAnnouncementFoldersFlow()
        val filterFlow = filterChannel.asFlow()

        return@withContext announcementFoldersFlow
            .combine(observeDocumentSortUseCase(), ::sortAnnouncementFolderList)
            .combine(filterFlow, ::filterAnnouncementFolderList)
    }

    override suspend fun filter(filterQuery: String) {
        filterChannel.send(filterQuery)
    }

    private fun filterAnnouncementFolderList(
        announcementFolderList: List<AnnouncementFolder>,
        query: String
    ): List<AnnouncementFolder> {
        return announcementFolderList.filter { announcementFolder ->
            if (query.isEmpty()) return@filter true
            val lastModifiedDate = dateFormatter(
                announcementFolder.lastModified,
                DateFormatter.ANNOUNCEMENT_DATE_FORMAT
            )
            announcementFolder.title.contains(query, true) ||
                    lastModifiedDate.contains(query, true)
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
            else -> throw IllegalArgumentException("No sorting type found with the name ${documentSort.type}")
        }.distinctBy(distinctSelector)
    }
}