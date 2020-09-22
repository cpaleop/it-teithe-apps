package gr.cpaleop.dashboard.data

import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.ANNOUNCEMENT_SORT_DESCENDING
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.ANNOUNCEMENT_SORT_TYPE
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.FILE_SORT_DESCENDING
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.FILE_SORT_TYPE
import gr.cpaleop.core.domain.repositories.PreferencesRepository.Companion.LANGUAGE
import gr.cpaleop.dashboard.domain.entities.AnnouncementSort
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesRepositoryImpl(private val preferencesRepository: gr.cpaleop.core.domain.repositories.PreferencesRepository) :
    PreferencesRepository {

    override suspend fun getPreferredLanguage(): String? = withContext(Dispatchers.IO) {
        preferencesRepository.getString(LANGUAGE)
    }

    override suspend fun getAnnouncementSort(): AnnouncementSort = withContext(Dispatchers.IO) {
        AnnouncementSort(
            type = preferencesRepository.getInt(ANNOUNCEMENT_SORT_TYPE),
            descending = preferencesRepository.getBoolean(ANNOUNCEMENT_SORT_DESCENDING),
            selected = true
        )
    }

    override suspend fun updateAnnouncementSort(announcementSort: AnnouncementSort) =
        withContext(Dispatchers.IO) {
            preferencesRepository.putInt(ANNOUNCEMENT_SORT_TYPE, announcementSort.type)
            preferencesRepository.putBoolean(
                ANNOUNCEMENT_SORT_DESCENDING,
                announcementSort.descending
            )
        }

    override suspend fun getDocumentSort(): DocumentSort = withContext(Dispatchers.IO) {
        DocumentSort(
            type = preferencesRepository.getInt(FILE_SORT_TYPE),
            descending = preferencesRepository.getBoolean(FILE_SORT_DESCENDING),
            selected = true
        )
    }

    override suspend fun updateDocumentSort(documentSort: DocumentSort) =
        withContext(Dispatchers.IO) {
            preferencesRepository.putInt(FILE_SORT_TYPE, documentSort.type)
            preferencesRepository.putBoolean(FILE_SORT_DESCENDING, documentSort.descending)
        }
}