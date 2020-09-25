package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine

@FlowPreview
@ExperimentalCoroutinesApi
class ObserveDocumentsUseCaseImpl(
    private val deviceStorageRepository: DeviceStorageRepository,
    private val dateFormatter: DateFormatter,
    private val observeDocumentSortUseCase: ObserveDocumentSortUseCase
) : ObserveDocumentsUseCase {

    private val filterChannel = ConflatedBroadcastChannel("")

    private val nameSelector: (Document) -> String = { document ->
        document.name
    }

    private val lastModifiedSelector: (Document) -> Long = { document ->
        document.lastModified
    }

    override suspend fun invoke(announcementId: String?): Flow<List<Document>> {
        val documentsFlow = when (announcementId) {
            null -> deviceStorageRepository.getDocumentsFlow()
            else -> deviceStorageRepository.getDocumentsByAnnouncementId(announcementId)
        }

        val filterFlow = filterChannel.asFlow()
        return documentsFlow
            .combine(filterFlow, ::filterDocumentList)
            .combine(observeDocumentSortUseCase(), ::sortDocumentList)
    }

    override suspend fun filter(filterQuery: String) {
        filterChannel.send(filterQuery)
    }

    private fun filterDocumentList(
        documentList: List<Document>,
        query: String
    ): List<Document> {
        return documentList.filter { document ->
            if (query.isEmpty()) return@filter true
            val lastModifiedDate = dateFormatter(
                document.lastModified,
                DateFormatter.ANNOUNCEMENT_DATE_FORMAT
            )
            document.name.contains(query, true) ||
                    document.uri.contains(query, true) ||
                    lastModifiedDate.contains(query, true)
        }
    }

    private fun sortDocumentList(
        documentList: List<Document>,
        documentSort: DocumentSort
    ): List<Document> {
        return when (documentSort.type) {
            DocumentSortType.ALPHABETICAL -> {
                if (documentSort.descending) {
                    documentList.sortedByDescending(nameSelector)
                } else {
                    documentList.sortedBy(nameSelector)
                }
            }
            DocumentSortType.DATE -> {
                if (documentSort.descending) {
                    documentList.sortedByDescending(
                        lastModifiedSelector
                    )
                } else {
                    documentList.sortedBy(lastModifiedSelector)
                }
            }
            else -> throw IllegalArgumentException("No sorting type found with the name ${documentSort.type}")
        }
    }
}