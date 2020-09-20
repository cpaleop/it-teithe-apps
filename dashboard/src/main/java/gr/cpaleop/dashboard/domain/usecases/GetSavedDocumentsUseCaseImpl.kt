package gr.cpaleop.dashboard.domain.usecases

import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository

class GetSavedDocumentsUseCaseImpl(
    private val deviceStorageRepository: DeviceStorageRepository,
    private val preferencesRepository: PreferencesRepository
) : GetSavedDocumentsUseCase {

    override suspend fun invoke(): List<Document> {
        val documentSortOption = preferencesRepository.getDocumentSort()
        return when (documentSortOption.type) {
            DocumentSortType.TYPE_ALPHABETICAL -> {
                if (documentSortOption.descending) {
                    deviceStorageRepository.getDocuments().sortedByDescending { document ->
                        document.name
                    }
                } else {
                    deviceStorageRepository.getDocuments().sortedBy { document ->
                        document.name
                    }
                }
            }
            DocumentSortType.TYPE_DATE -> {
                if (documentSortOption.descending) {
                    deviceStorageRepository.getDocuments().sortedByDescending { document ->
                        document.lastModified
                    }
                } else {
                    deviceStorageRepository.getDocuments().sortedBy { document ->
                        document.lastModified
                    }
                }
            }
            else -> throw IllegalArgumentException("No sorting type found with the name ${documentSortOption.type}")
        }
    }
}