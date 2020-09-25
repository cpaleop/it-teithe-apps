package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.DateFormatter
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class ObserveDocumentsUseCaseImpl(
    private val deviceStorageRepository: DeviceStorageRepository,
    private val preferencesRepository: PreferencesRepository,
    private val dateFormatter: DateFormatter
) : ObserveDocumentsUseCase {

    private val filterChannel = ConflatedBroadcastChannel("")

    private val nameSelector: (Document) -> String = { document ->
        document.name
    }

    private val lastModifiedSelector: (Document) -> Long = { document ->
        document.lastModified
    }

    override suspend fun invoke(announcementId: String?): Flow<List<Document>> {
        val documentSort = preferencesRepository.getDocumentSort()
        val documentsFlow = when (announcementId) {
            null -> deviceStorageRepository.getDocumentsFlow()
            else -> deviceStorageRepository.getDocumentsByAnnouncementId(announcementId)
        }

        val filterFlow = filterChannel.asFlow()

        return documentsFlow.map { documents ->
            when (documentSort.type) {
                DocumentSortType.ALPHABETICAL -> {
                    if (documentSort.descending) {
                        documents.sortedByDescending(nameSelector)
                    } else {
                        documents.sortedBy(nameSelector)
                    }
                }
                DocumentSortType.DATE -> {
                    if (documentSort.descending) {
                        documents.sortedByDescending(
                            lastModifiedSelector
                        )
                    } else {
                        documents.sortedBy(lastModifiedSelector)
                    }
                }
                else -> throw IllegalArgumentException("No sorting type found with the name ${documentSort.type}")
            }
        }.combine(filterFlow) { documentList, query ->
            documentList.filter { document ->
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
    }

    override suspend fun filter(filterQuery: String) {
        filterChannel.send(filterQuery)
    }
}