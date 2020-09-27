package gr.cpaleop.dashboard.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.repositories.DeviceStorageRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class ObserveDocumentsAnnouncementFoldersUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var deviceStorageRepository: DeviceStorageRepository

    @MockK
    private lateinit var observeDocumentSortUseCase: ObserveDocumentSortUseCase

    private lateinit var observeDocumentsAnnouncementFoldersUseCase: ObserveDocumentsAnnouncementFoldersUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        observeDocumentsAnnouncementFoldersUseCase = ObserveDocumentsAnnouncementFoldersUseCaseImpl(
            testDefaultCoroutineDispatcher,
            deviceStorageRepository,
            observeDocumentSortUseCase
        )
    }

    /**
     * We cannot test throwable's equality.
     * Instead, we test its instance and its carrying message
     */
    @Test
    fun `invoke throws exception when DocumentSortType not found`() = runBlocking {
        val result = kotlin.runCatching {
            coEvery { observeDocumentSortUseCase() } returns SORT_TYPE_UNKNOWN_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            observeDocumentsAnnouncementFoldersUseCase().first()
        }
        assertThat(result.isFailure).isEqualTo(true)
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("No sorting type found with the value -10")
    }

    @Test
    fun `invoke returns distinct and sorted list when sort type is date descending`() =
        runBlocking {
            val expected = announcementFoldersDateDescending
            coEvery { observeDocumentSortUseCase() } returns SORT_DATE_DESCENDING_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            val actual = observeDocumentsAnnouncementFoldersUseCase().first()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `invoke returns distinct and sorted list when sort type is date ascending`() = runBlocking {
        val expected = announcementFoldersDateAscending
        coEvery { observeDocumentSortUseCase() } returns SORT_DATE_ASCENDING_FLOW
        coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
        val actual = observeDocumentsAnnouncementFoldersUseCase().first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke returns distinct and sorted list when sort type is alphabetical descending`() =
        runBlocking {
            val expected = announcementFoldersNameDescending
            coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_DESCENDING_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            val actual = observeDocumentsAnnouncementFoldersUseCase().first()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `invoke returns distinct and sorted list when sort type is alphabetical ascending`() =
        runBlocking {
            val expected = announcementFoldersNameAscending
            coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_ASCENDING_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            val actual = observeDocumentsAnnouncementFoldersUseCase().first()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `filter when not empty and sort is ALPHABETICAL ASCENDING returns correct values`() =
        runBlocking {
            val givenFilterQuery = "nameb"
            val expected = announcementFoldersFilteredTitleAscending
            coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_ASCENDING_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            observeDocumentsAnnouncementFoldersUseCase.filter(givenFilterQuery)
            val actual = observeDocumentsAnnouncementFoldersUseCase().first()
            assertThat(actual).isEqualTo(expected)
        }

    // Make use of reversed list function to reuse dataset
    @Test
    fun `filter when not empty and sort is ALPHABETICAL DESCENDING returns correct values`() =
        runBlocking {
            val givenFilterQuery = "nameb"
            val expected = announcementFoldersFilteredTitleAscending.reversed()
            coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_DESCENDING_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            observeDocumentsAnnouncementFoldersUseCase.filter(givenFilterQuery)
            val actual = observeDocumentsAnnouncementFoldersUseCase().first()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `filter when not empty and sort is DATE ASCENDING returns correct values`() =
        runBlocking {
            val givenFilterQuery = "nameb"
            val expected = announcementFoldersFilteredDateAscending
            coEvery { observeDocumentSortUseCase() } returns SORT_DATE_ASCENDING_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            observeDocumentsAnnouncementFoldersUseCase.filter(givenFilterQuery)
            val actual = observeDocumentsAnnouncementFoldersUseCase().first()
            assertThat(actual).isEqualTo(expected)
        }

    // Make use of reversed list function to reuse dataset
    @Test
    fun `filter when not empty and sort is DATE DESCENDING returns correct values`() =
        runBlocking {
            val givenFilterQuery = "nameb"
            val expected = announcementFoldersFilteredDateAscending.reversed()
            coEvery { observeDocumentSortUseCase() } returns SORT_DATE_DESCENDING_FLOW
            coEvery { deviceStorageRepository.getAnnouncementFoldersFlow() } returns announcementFoldersFlow
            observeDocumentsAnnouncementFoldersUseCase.filter(givenFilterQuery)
            val actual = observeDocumentsAnnouncementFoldersUseCase().first()
            assertThat(actual).isEqualTo(expected)
        }

    companion object {

        private val SORT_TYPE_UNKNOWN_FLOW = flow {
            emit(
                DocumentSort(
                    type = -10,
                    selected = true,
                    descending = true
                )
            )
        }

        private val SORT_DATE_DESCENDING_FLOW = flow {
            emit(
                DocumentSort(
                    type = DocumentSortType.DATE,
                    selected = true,
                    descending = true
                )
            )
        }

        private val SORT_DATE_ASCENDING_FLOW = flow {
            emit(
                DocumentSort(
                    type = DocumentSortType.DATE,
                    selected = true,
                    descending = false
                )
            )
        }

        private val SORT_ALPHABETICAL_DESCENDING_FLOW = flow {
            emit(
                DocumentSort(
                    type = DocumentSortType.ALPHABETICAL,
                    selected = true,
                    descending = true
                )
            )
        }

        private val SORT_ALPHABETICAL_ASCENDING_FLOW = flow {
            emit(
                DocumentSort(
                    type = DocumentSortType.ALPHABETICAL,
                    selected = true,
                    descending = false
                )
            )
        }

        private val announcementFolders = listOf(
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
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
                id = "id2",
                title = "namea",
                lastModified = 2L
            ),
            AnnouncementFolder(
                id = "id4",
                title = "named",
                lastModified = 4L
            ),
            AnnouncementFolder(
                id = "id4",
                title = "named",
                lastModified = 4L
            ),
            AnnouncementFolder(
                id = "id5",
                title = "namebAnother",
                lastModified = 4L
            )
        )

        private val announcementFoldersFilteredTitleAscending = listOf(
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id5",
                title = "namebAnother",
                lastModified = 4L
            )
        )

        private val announcementFoldersFilteredDateAscending = listOf(
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id5",
                title = "namebAnother",
                lastModified = 4L
            )
        )

        private val announcementFoldersFlow = flow {
            emit(announcementFolders)
        }

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
            ),
            AnnouncementFolder(
                id = "id5",
                title = "namebAnother",
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
                id = "id5",
                title = "namebAnother",
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
                id = "id5",
                title = "namebAnother",
                lastModified = 4L
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
                title = "named",
                lastModified = 4L
            ),
            AnnouncementFolder(
                id = "id3",
                title = "namec",
                lastModified = 3L
            ),
            AnnouncementFolder(
                id = "id5",
                title = "namebAnother",
                lastModified = 4L
            ),
            AnnouncementFolder(
                id = "id1",
                title = "nameb",
                lastModified = 1L
            ),
            AnnouncementFolder(
                id = "id2",
                title = "namea",
                lastModified = 2L
            )
        )
    }
}