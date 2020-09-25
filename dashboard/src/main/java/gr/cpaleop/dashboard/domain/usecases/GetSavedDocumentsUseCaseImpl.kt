package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetSavedDocumentsUseCaseImpl(
    private val deviceStorageRepository: DeviceStorageRepository,
    private val preferencesRepository: PreferencesRepository
) : GetSavedDocumentsUseCase {

    override suspend fun invoke(announcementId: String?): List<Document> {
        val documentSortOption = preferencesRepository.getDocumentSort()
        val documents = when (announcementId) {
            null -> deviceStorageRepository.getDocuments()
            else -> deviceStorageRepository.getDocumentsByAnnouncementId(announcementId)
        }

        return when (documentSortOption.type) {
            DocumentSortType.ALPHABETICAL -> {
                if (documentSortOption.descending) {
                    documents.sortedByDescending { document ->
                        document.name
                    }
                } else {
                    documents.sortedBy { document ->
                        document.name
                    }
                }
            }
            DocumentSortType.DATE -> {
                if (documentSortOption.descending) {
                    documents.sortedByDescending { document ->
                        document.lastModified
                    }
                } else {
                    documents.sortedBy { document ->
                        document.lastModified
                    }
                }
            }
            else -> throw IllegalArgumentException("No sorting type found with the name ${documentSortOption.type}")
        }
    }
}