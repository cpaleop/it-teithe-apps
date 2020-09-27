package gr.cpaleop.dashboard.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.domain.entities.Document
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

@ExperimentalCoroutinesApi
@FlowPreview
class ObserveDocumentsUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var deviceStorageRepository: DeviceStorageRepository

    @MockK
    private lateinit var observeDocumentSortUseCase: ObserveDocumentSortUseCase

    private lateinit var observeDocumentsUseCase: ObserveDocumentsUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        observeDocumentsUseCase = ObserveDocumentsUseCaseImpl(
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
            coEvery { deviceStorageRepository.getDocumentsFlow() } returns documentListFlow
            observeDocumentsUseCase(null).first()
        }
        assertThat(result.isFailure).isEqualTo(true)
        assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(result.exceptionOrNull()?.message).isEqualTo("No sorting type found with the value -10")
    }

    @Test
    fun `invoke when sort is DATE_ASCENDING returns correct items`() = runBlocking {
        val expected = documentListDateAscending
        coEvery { observeDocumentSortUseCase() } returns SORT_DATE_ASCENDING_FLOW
        coEvery { deviceStorageRepository.getDocumentsFlow() } returns documentListFlow
        val actual = observeDocumentsUseCase(null).first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke when sort is DATE_DESCENDING returns correct items`() = runBlocking {
        val expected = documentListDateDescending
        coEvery { observeDocumentSortUseCase() } returns SORT_DATE_DESCENDING_FLOW
        coEvery { deviceStorageRepository.getDocumentsFlow() } returns documentListFlow
        val actual = observeDocumentsUseCase(null).first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke when sort is ALPHABETICAL_ASCENDING returns correct items`() = runBlocking {
        val expected = documentListNameAscending
        coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_ASCENDING_FLOW
        coEvery { deviceStorageRepository.getDocumentsFlow() } returns documentListFlow
        val actual = observeDocumentsUseCase(null).first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke when sort is ALPHABETICAL_DESCENDING returns correct items`() = runBlocking {
        val expected = documentListNameDescending
        coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_DESCENDING_FLOW
        coEvery { deviceStorageRepository.getDocumentsFlow() } returns documentListFlow
        val actual = observeDocumentsUseCase(null).first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `filter when not empty and sort is ALPHABETICAL_DESCENDING returns correct items`() =
        runBlocking {
            val givenQuery = "name1"
            val expected = documentListFiltered
            coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_DESCENDING_FLOW
            coEvery { deviceStorageRepository.getDocumentsFlow() } returns documentListFlow
            observeDocumentsUseCase.filter(givenQuery)
            val actual = observeDocumentsUseCase(null).first()
            assertThat(actual).isEqualTo(expected)
        }

    @Test
    fun `filter when empty and sort is ALPHABETICAL_DESCENDING returns original items`() =
        runBlocking {
            val givenQuery = ""
            val expected = documentListNameDescending
            coEvery { observeDocumentSortUseCase() } returns SORT_ALPHABETICAL_DESCENDING_FLOW
            coEvery { deviceStorageRepository.getDocumentsFlow() } returns documentListFlow
            observeDocumentsUseCase.filter(givenQuery)
            val actual = observeDocumentsUseCase(null).first()
            assertThat(actual).isEqualTo(expected)
        }

    companion object {

        private val documentListFlow = flow {
            emit(documentList)
        }
        private val documentList = listOf(
            Document(
                uri = "uri1",
                announcementId = "id1",
                absolutePath = "folder/name1",
                name = "name1",
                type = "pdf",
                size = 100L,
                previewUri = "preview_uri/name1",
                lastModified = 100L
            ),
            Document(
                uri = "uri2",
                announcementId = "id2",
                absolutePath = "folder/name2",
                name = "name2",
                type = "pdf",
                size = 200L,
                previewUri = "preview_uri/name2",
                lastModified = 200L
            )
        )

        private val documentListDateAscending = listOf(
            Document(
                uri = "uri1",
                announcementId = "id1",
                absolutePath = "folder/name1",
                name = "name1",
                type = "pdf",
                size = 100L,
                previewUri = "preview_uri/name1",
                lastModified = 100L
            ),
            Document(
                uri = "uri2",
                announcementId = "id2",
                absolutePath = "folder/name2",
                name = "name2",
                type = "pdf",
                size = 200L,
                previewUri = "preview_uri/name2",
                lastModified = 200L
            )
        )

        private val documentListDateDescending = listOf(
            Document(
                uri = "uri2",
                announcementId = "id2",
                absolutePath = "folder/name2",
                name = "name2",
                type = "pdf",
                size = 200L,
                previewUri = "preview_uri/name2",
                lastModified = 200L
            ),
            Document(
                uri = "uri1",
                announcementId = "id1",
                absolutePath = "folder/name1",
                name = "name1",
                type = "pdf",
                size = 100L,
                previewUri = "preview_uri/name1",
                lastModified = 100L
            )
        )

        private val documentListNameAscending = listOf(
            Document(
                uri = "uri1",
                announcementId = "id1",
                absolutePath = "folder/name1",
                name = "name1",
                type = "pdf",
                size = 100L,
                previewUri = "preview_uri/name1",
                lastModified = 100L
            ),
            Document(
                uri = "uri2",
                announcementId = "id2",
                absolutePath = "folder/name2",
                name = "name2",
                type = "pdf",
                size = 200L,
                previewUri = "preview_uri/name2",
                lastModified = 200L
            )
        )

        private val documentListNameDescending = listOf(
            Document(
                uri = "uri2",
                announcementId = "id2",
                absolutePath = "folder/name2",
                name = "name2",
                type = "pdf",
                size = 200L,
                previewUri = "preview_uri/name2",
                lastModified = 200L
            ),
            Document(
                uri = "uri1",
                announcementId = "id1",
                absolutePath = "folder/name1",
                name = "name1",
                type = "pdf",
                size = 100L,
                previewUri = "preview_uri/name1",
                lastModified = 100L
            )
        )

        private val documentListFilteredFlow = flow { emit(documentListFiltered) }
        private val documentListFiltered = listOf(
            Document(
                uri = "uri1",
                announcementId = "id1",
                absolutePath = "folder/name1",
                name = "name1",
                type = "pdf",
                size = 100L,
                previewUri = "preview_uri/name1",
                lastModified = 100L
            )
        )

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
    }
}