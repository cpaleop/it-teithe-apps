package gr.cpaleop.documents.domain.usecases

import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.documents.domain.repositories.DeviceStorageRepository
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
class ObserveDocumentsUseCaseImpl(
    @DefaultDispatcher
    private val defaultDispatcher: CoroutineDispatcher,
    private val deviceStorageRepository: DeviceStorageRepository,
    private val observeDocumentSortUseCase: ObserveDocumentSortUseCase
) : ObserveDocumentsUseCase {

    private val filterChannel = ConflatedBroadcastChannel("")

    private val nameSelector: (Document) -> String = { document ->
        document.name
    }

    private val lastModifiedSelector: (Document) -> Long = { document ->
        document.lastModified
    }

    override suspend fun invoke(announcementId: String?): Flow<List<Document>> =
        withContext(defaultDispatcher) {
            val documentsFlow = when (announcementId) {
                null -> deviceStorageRepository.getDocumentsFlow()
                else -> deviceStorageRepository.getDocumentsByAnnouncementId(announcementId)
            }

            val filterFlow = filterChannel.asFlow()
            return@withContext documentsFlow
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
            document.name.contains(query, true)
        }
    }

    private fun sortDocumentList(
        documentList: List<Document>,
        documentSort: gr.cpaleop.documents.domain.entities.DocumentSort
    ): List<Document> {
        return when (documentSort.type) {
            gr.cpaleop.documents.domain.entities.DocumentSortType.ALPHABETICAL -> {
                if (documentSort.descending) {
                    documentList.sortedByDescending(nameSelector)
                } else {
                    documentList.sortedBy(nameSelector)
                }
            }
            gr.cpaleop.documents.domain.entities.DocumentSortType.DATE -> {
                if (documentSort.descending) {
                    documentList.sortedByDescending(
                        lastModifiedSelector
                    )
                } else {
                    documentList.sortedBy(lastModifiedSelector)
                }
            }
            else -> throw IllegalArgumentException("No sorting type found with the value ${documentSort.type}")
        }
    }
}