package gr.cpaleop.dashboard.presentation.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import gr.cpaleop.dashboard.domain.repositories.PreferencesRepository
import gr.cpaleop.dashboard.domain.usecases.ObserveDocumentsAnnouncementFoldersUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ObserveDocumentsAnnouncementFoldersUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var deviceStorageRepository: DeviceStorageRepository

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    private lateinit var observeDocumentsAnnouncementFoldersUseCase: ObserveDocumentsAnnouncementFoldersUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        observeDocumentsAnnouncementFoldersUseCase = ObserveDocumentsAnnouncementFoldersUseCaseImpl(
            testDefaultCoroutineDispatcher,
            deviceStorageRepository,
            preferencesRepository
        )
    }

    @Test
    fun `invoke returns distinct and sorted list when sort type is date descending`() =
        runBlocking {
            val expected = announcementFoldersDateDescending
            coEvery { preferencesRepository.getDocumentSort() } returns SORT_DATE_DESCENDING
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFolders

            val actual = observeDocumentsAnnouncementFoldersUseCase()

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `invoke returns distinct and sorted list when sort type is date ascending`() = runBlocking {
        val expected = announcementFoldersDateAscending
        coEvery { preferencesRepository.getDocumentSort() } returns SORT_DATE_ASCENDING
        coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFolders

        val actual = observeDocumentsAnnouncementFoldersUseCase()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke returns distinct and sorted list when sort type is alphabetical descending`() =
        runBlocking {
            val expected = announcementFoldersNameDescending
            coEvery { preferencesRepository.getDocumentSort() } returns SORT_ALPHABETICAL_DESCENDING
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFolders

            val actual = observeDocumentsAnnouncementFoldersUseCase()

            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `invoke returns distinct and sorted list when sort type is alphabetical ascending`() =
        runBlocking {
            val expected = announcementFoldersNameAscending
            coEvery { preferencesRepository.getDocumentSort() } returns SORT_ALPHABETICAL_ASCENDING
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFolders

            val actual = observeDocumentsAnnouncementFoldersUseCase()

            assertThat(actual).isEqualTo(expected)
        }

    companion object {

        private val SORT_DATE_DESCENDING = DocumentSort(
            type = DocumentSortType.DATE,
            selected = true,
            descending = true
        )

        private val SORT_DATE_ASCENDING = DocumentSort(
            type = DocumentSortType.DATE,
            selected = true,
            descending = false
        )

        private val SORT_ALPHABETICAL_DESCENDING = DocumentSort(
            type = DocumentSortType.ALPHABETICAL,
            selected = true,
            descending = true
        )

        private val SORT_ALPHABETICAL_ASCENDING = DocumentSort(
            type = DocumentSortType.ALPHABETICAL,
            selected = true,
            descending = false
        )

        private val announcementFolders = listOf(
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id1",
                title = "namebDuplicate",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id3",
                title = "namec",
                lastModified = 3L
            ),
            AnnouncementFolder(
                id = "id3",
                title = "namecDuplicate",
                lastModified = 3L
            ),
            AnnouncementFolder(
                id = "id2",
                title = "namea",
                lastModified = 2L
            ),
            AnnouncementFolder(
                id = "id2",
                title = "nameaDuplicate",
                lastModified = 2L
            ),
            AnnouncementFolder(
                id = "id4",
                title = "named",
                lastModified = 4L
            ),
            AnnouncementFolder(
                id = "id4",
                title = "namedDuplicate",
                lastModified = 4L
            )
        )

        private val announcementFoldersDateAscending = listOf(
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id2",
                title = "namea",
                lastModified = 2L
            ),
            AnnouncementFolder(
                id = "id3",
                title = "namec",
                lastModified = 3L
            ),
            AnnouncementFolder(
                id = "id4",
                title = "named",
                lastModified = 4L
            )
        )

        private val announcementFoldersDateDescending = listOf(
            AnnouncementFolder(
                id = "id4",
                title = "named",
                lastModified = 4L
            ),
            AnnouncementFolder(
                id = "id3",
                title = "namec",
                lastModified = 3L
            ),
            AnnouncementFolder(
                id = "id2",
                title = "namea",
                lastModified = 2L
            ),
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
            )
        )

        private val announcementFoldersNameAscending = listOf(
            AnnouncementFolder(
                id = "id2",
                title = "namea",
                lastModified = 2L
            ),
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id3",
                title = "namec",
                lastModified = 3L
            ),
            AnnouncementFolder(
                id = "id4",
                title = "named",
                lastModified = 4L
            )
        )

        private val announcementFoldersNameDescending = listOf(
            AnnouncementFolder(
                id = "id4",
                title = "namedDuplicate",
                lastModified = 4L
            ),
            AnnouncementFolder(
                id = "id3",
                title = "namecDuplicate",
                lastModified = 3L
            ),
            AnnouncementFolder(
                id = "id1",
                title = "namebDuplicate",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id2",
                title = "nameaDuplicate",
                lastModified = 2L
            )
        )
    }
}