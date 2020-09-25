package gr.cpaleop.dashboard.presentation.documents

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.domain.usecases.*
import gr.cpaleop.dashboard.presentation.documents.document.FileDocument
import gr.cpaleop.dashboard.presentation.documents.document.FileDocumentMapper
import gr.cpaleop.dashboard.presentation.documents.options.DocumentDetails
import gr.cpaleop.dashboard.presentation.documents.options.DocumentOption
import gr.cpaleop.dashboard.presentation.documents.options.DocumentOptionMapper
import gr.cpaleop.dashboard.presentation.documents.sort.DocumentSortOption
import gr.cpaleop.dashboard.presentation.documents.sort.DocumentSortOptionMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Notice:
 * [DocumentsViewModel.handleDocumentOptionChoice] for type SHARE needs to be Instrumentation tested separately
 */
@ExperimentalCoroutinesApi
class DocumentsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    val testMainCoroutineDispatcher = TestCoroutineDispatcher()

    @DefaultDispatcher
    val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getSavedDocumentsUseCase: GetSavedDocumentsUseCase

    @MockK
    private lateinit var fileDocumentMapper: FileDocumentMapper

    @MockK
    private lateinit var getDocumentOptionsUseCase: GetDocumentOptionsUseCase

    @MockK
    private lateinit var documentOptionMapper: DocumentOptionMapper

    @MockK
    private lateinit var getDocumentUseCase: GetDocumentUseCase

    @MockK
    private lateinit var deleteDocumentUseCase: DeleteDocumentUseCase

    @MockK
    private lateinit var renameDocumentUseCase: RenameDocumentUseCase

    @MockK
    private lateinit var getDocumentSortOptionsUseCase: GetDocumentSortOptionsUseCase

    @MockK
    private lateinit var documentSortOptionMapper: DocumentSortOptionMapper

    @MockK
    private lateinit var updateDocumentSortUseCase: UpdateDocumentSortUseCase

    @MockK
    private lateinit var getDocumentSortUseCase: GetDocumentSortUseCase

    private lateinit var viewModel: DocumentsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = DocumentsViewModel(
            testMainCoroutineDispatcher,
            testDefaultCoroutineDispatcher,
            getSavedDocumentsUseCase,
            fileDocumentMapper,
            getDocumentOptionsUseCase,
            documentOptionMapper,
            getDocumentUseCase,
            deleteDocumentUseCase,
            renameDocumentUseCase,
            getDocumentSortOptionsUseCase,
            documentSortOptionMapper,
            updateDocumentSortUseCase,
            getDocumentSortUseCase
        )
    }

    @Test
    fun `presentDocuments success with no empty list`() {
        val expected = fileDocumentList
        coEvery { getSavedDocumentsUseCase() } returns documentList
        coEvery { fileDocumentMapper(documentList[0]) } returns fileDocumentList[0]
        coEvery { fileDocumentMapper(documentList[1]) } returns fileDocumentList[1]
        viewModel.presentDocuments()
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsEmpty)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentDocuments success with empty list`() {
        val expected = emptyList<FileDocument>()
        coEvery { getSavedDocumentsUseCase() } returns emptyList()
        viewModel.presentDocuments()
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsEmpty)).isEqualTo(true)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentDocuments when fails catches exception`() {
        coEvery { getSavedDocumentsUseCase() } throws Throwable()
        viewModel.presentDocuments()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    /**
     * We need to call [DocumentsViewModel.presentDocuments] in order to populate livedata and search them
     */
    @Test
    fun `searchDocuments when given query success with multiple results`() {
        coEvery { getSavedDocumentsUseCase() } returns documentList
        coEvery { fileDocumentMapper(documentList[0]) } returns fileDocumentList[0]
        coEvery { fileDocumentMapper(documentList[1]) } returns fileDocumentList[1]
        viewModel.presentDocuments()
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(fileDocumentList)

        val givenQuery = "name"
        val expected = listOf(fileDocumentList[0], fileDocumentList[1])
        viewModel.searchDocuments(givenQuery)
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsFilterEmpty)).isEqualTo(false)
    }

    /**
     * We need to call [DocumentsViewModel.presentDocuments] in order to populate livedata and search them
     */
    @Test
    fun `searchDocuments when given query success with one result`() {
        coEvery { getSavedDocumentsUseCase() } returns documentList
        coEvery { fileDocumentMapper(documentList[0]) } returns fileDocumentList[0]
        coEvery { fileDocumentMapper(documentList[1]) } returns fileDocumentList[1]
        viewModel.presentDocuments()
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(fileDocumentList)

        val givenQuery = "name1"
        val expected = listOf(fileDocumentList[1])
        viewModel.searchDocuments(givenQuery)
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsFilterEmpty)).isEqualTo(false)
    }

    /**
     * We need to call [DocumentsViewModel.presentDocuments] in order to populate livedata and search them
     */
    @Test
    fun `searchDocuments when given query success with empty list`() {
        coEvery { getSavedDocumentsUseCase() } returns documentList
        coEvery { fileDocumentMapper(documentList[0]) } returns fileDocumentList[0]
        coEvery { fileDocumentMapper(documentList[1]) } returns fileDocumentList[1]
        viewModel.presentDocuments()
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(fileDocumentList)

        val givenQuery = "name111"
        val expected = emptyList<FileDocument>()
        viewModel.searchDocuments(givenQuery)
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsFilterEmpty)).isEqualTo(true)
    }

    @Test
    fun `presentDocumentDetails success`() {
        val givenUri = "uri"
        val expected = documentList[0]
        coEvery { getDocumentUseCase(givenUri) } returns documentList[0]
        viewModel.presentDocumentDetails(givenUri)
        assertThat(LiveDataTest.getValue(viewModel.document)).isEqualTo(expected)
    }

    @Test
    fun `presentDocumentDetails when fails catches exception`() {
        val givenUri = "uri"
        coEvery { getDocumentUseCase(givenUri) } throws Throwable()
        viewModel.presentDocumentDetails(givenUri)
    }

    @Test
    fun `presentDocumentOptions success`() {
        val expected = documentOptionList
        coEvery { getDocumentOptionsUseCase() } returns documentOptionTypeList
        every { documentOptionMapper(documentOptionTypeList[0]) } returns documentOptionList[0]
        every { documentOptionMapper(documentOptionTypeList[1]) } returns documentOptionList[1]
        every { documentOptionMapper(documentOptionTypeList[2]) } returns documentOptionList[2]
        every { documentOptionMapper(documentOptionTypeList[3]) } returns documentOptionList[3]
        viewModel.presentDocumentOptions()
        assertThat(LiveDataTest.getValue(viewModel.documentOptions)).isEqualTo(expected)
    }

    @Test
    fun `presentDocumentOptions when fails catches exception`() {
        coEvery { getDocumentOptionsUseCase() } throws Throwable()
        viewModel.presentDocumentOptions()
    }

    /**
     * We need to call [DocumentsViewModel.presentDocumentDetails] in order to populate livedata and search them
     */
    @Test
    fun `handleDocumentOptionChoice when option is ANNOUNCEMENT`() {
        val givenUri = "uri"
        val document = documentList[0]
        coEvery { getDocumentUseCase(givenUri) } returns documentList[0]
        viewModel.presentDocumentDetails(givenUri)
        assertThat(LiveDataTest.getValue(viewModel.document)).isEqualTo(document)

        val expected = document.announcementId
        viewModel.handleDocumentOptionChoice(DocumentOptionType.ANNOUNCEMENT)
        assertThat(LiveDataTest.getValue(viewModel.optionNavigateAnnouncement)).isEqualTo(expected)
    }

    /**
     * We need to call [DocumentsViewModel.presentDocumentDetails] in order to populate livedata and search them
     */
    @Test
    fun `handleDocumentOptionChoice when option is RENAME`() {
        val givenUri = "uri"
        val document = documentList[0]
        coEvery { getDocumentUseCase(givenUri) } returns documentList[0]
        viewModel.presentDocumentDetails(givenUri)
        assertThat(LiveDataTest.getValue(viewModel.document)).isEqualTo(document)

        val expected = DocumentDetails(uri = document.uri, name = document.name)
        viewModel.handleDocumentOptionChoice(DocumentOptionType.RENAME)
        assertThat(LiveDataTest.getValue(viewModel.optionRename)).isEqualTo(expected)
    }

    /**
     * We need to call [DocumentsViewModel.presentDocumentDetails] in order to populate livedata and search them
     */
    @Test
    fun `handleDocumentOptionChoice when option is DELETE`() {
        val givenUri = "uri"
        val document = documentList[0]
        coEvery { getDocumentUseCase(givenUri) } returns documentList[0]
        viewModel.presentDocumentDetails(givenUri)
        assertThat(LiveDataTest.getValue(viewModel.document)).isEqualTo(document)

        val expected = DocumentDetails(uri = document.uri, name = document.name)
        viewModel.handleDocumentOptionChoice(DocumentOptionType.DELETE)
        assertThat(LiveDataTest.getValue(viewModel.optionDelete)).isEqualTo(expected)
    }

    @Test
    fun `deleteDocument success`() {
        val givenUri = "uri"
        val expected = Unit
        coEvery { deleteDocumentUseCase(givenUri) } returns Unit
        viewModel.deleteDocument(givenUri)
        assertThat(LiveDataTest.getValue(viewModel.refresh)).isEqualTo(expected)
    }

    @Test
    fun `deleteDocument when fails catches exception`() {
        val givenUri = "uri"
        coEvery { deleteDocumentUseCase(givenUri) } throws Throwable()
        viewModel.deleteDocument(givenUri)
    }

    @Test
    fun `renameDocument success`() {
        val givenUri = "uri"
        val givenNewName = "newName"
        val expected = Unit
        coEvery { renameDocumentUseCase(givenUri, givenNewName) } returns Unit
        viewModel.renameDocument(givenUri, givenNewName)
        assertThat(LiveDataTest.getValue(viewModel.refresh)).isEqualTo(expected)
    }

    @Test
    fun `renameDocument when fails catches exception`() {
        val givenUri = "uri"
        val givenNewName = "newName"
        coEvery { renameDocumentUseCase(givenUri, givenNewName) } throws Throwable()
        viewModel.renameDocument(givenUri, givenNewName)
    }

    @Test
    fun `presentDocumentSortSelected success`() {
        val expected = selectedDocumentSortOption
        coEvery { getDocumentSortUseCase() } returns selectedDocumentSort
        every { documentSortOptionMapper(selectedDocumentSort) } returns selectedDocumentSortOption
        viewModel.presentDocumentSortSelected()
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptionSelected)).isEqualTo(expected)
    }

    @Test
    fun `presentDocumentSortSelected when fails catches exception`() {
        coEvery { getDocumentSortUseCase() } throws Throwable()
        viewModel.presentDocumentSortSelected()
    }

    @Test
    fun `presentDocumentSortOptions success`() {
        val expected = documentSortOptionsList
        val expectedSortOptionSelected = documentSortOptionsList[0]
        coEvery { getDocumentSortOptionsUseCase() } returns documentSortList
        every { documentSortOptionMapper(documentSortList[0]) } returns documentSortOptionsList[0]
        every { documentSortOptionMapper(documentSortList[1]) } returns documentSortOptionsList[1]
        viewModel.presentDocumentSortOptions()
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptions)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptionSelected)).isEqualTo(
            expectedSortOptionSelected
        )
    }

    @Test
    fun `presentDocumentSortOptions when fails catches exception`() {
        coEvery { getDocumentSortOptionsUseCase() } throws Throwable()
        viewModel.presentDocumentSortOptions()
    }

    @Test
    fun `updateSort success`() {
        val givenType = DocumentSortType.DATE
        val givenDescending = true
        val givenSelected = true
        val givenDocumentSort = DocumentSort(givenType, givenSelected, givenDescending)
        val expected = selectedDocumentSortOption
        coEvery { updateDocumentSortUseCase(givenDocumentSort) } returns selectedDocumentSort
        every { documentSortOptionMapper(selectedDocumentSort) } returns selectedDocumentSortOption
        viewModel.updateSort(givenDocumentSort)
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptionSelected)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.refresh)).isEqualTo(Unit)
    }

    @Test
    fun `updateSort when fails catches exception`() {
        val givenType = DocumentSortType.DATE
        val givenDescending = true
        val givenSelected = true
        val givenDocumentSort = DocumentSort(givenType, givenSelected, givenDescending)
        coEvery { updateDocumentSortUseCase(givenDocumentSort) } throws Throwable()
        viewModel.updateSort(givenDocumentSort)
    }

    companion object {

        private val documentSortOptionsList = listOf(
            DocumentSortOption(
                type = DocumentSortType.DATE,
                imageResource = R.drawable.ic_arrow_down,
                labelResource = R.string.documents_sort_date,
                selected = true,
                descending = true
            ),
            DocumentSortOption(
                type = DocumentSortType.ALPHABETICAL,
                imageResource = R.drawable.ic_arrow_up,
                labelResource = R.string.documents_sort_alphabetical,
                selected = false,
                descending = false
            )
        )

        private val documentSortList = listOf(
            DocumentSort(
                type = DocumentSortType.DATE,
                selected = true,
                descending = true
            ),
            DocumentSort(
                type = DocumentSortType.ALPHABETICAL,
                selected = false,
                descending = false
            )
        )

        private val selectedDocumentSortOption = DocumentSortOption(
            type = DocumentSortType.DATE,
            imageResource = R.drawable.ic_arrow_down,
            labelResource = R.string.documents_sort_date,
            selected = true,
            descending = true
        )

        private val selectedDocumentSort = DocumentSort(
            type = DocumentSortType.DATE,
            selected = true,
            descending = true
        )

        private val documentOptionList = listOf(
            DocumentOption(
                type = DocumentOptionType.ANNOUNCEMENT,
                name = R.string.documents_option_announcement,
                iconResource = R.drawable.ic_link
            ),
            DocumentOption(
                type = DocumentOptionType.RENAME,
                name = R.string.documents_option_rename,
                iconResource = R.drawable.ic_edit
            ),
            DocumentOption(
                type = DocumentOptionType.DELETE,
                name = R.string.documents_option_delete,
                iconResource = R.drawable.ic_delete
            ),
            DocumentOption(
                type = DocumentOptionType.SHARE,
                name = R.string.documents_option_share,
                iconResource = R.drawable.ic_share
            )
        )

        private val documentOptionTypeList = listOf(
            DocumentOptionType.ANNOUNCEMENT,
            DocumentOptionType.RENAME,
            DocumentOptionType.DELETE,
            DocumentOptionType.SHARE
        )

        private val documentList = listOf(
            Document(
                uri = "uri",
                name = "name",
                size = 10000L,
                absolutePath = "absolute_path/name",
                type = "pdf",
                lastModified = 10000L,
                previewUri = "preview_uri",
                announcementId = "announcement_id"
            ),
            Document(
                uri = "uri1",
                name = "name1",
                size = 10000L,
                absolutePath = "absolute_path/name1",
                type = "docx",
                lastModified = 10000L,
                previewUri = "preview_uri1",
                announcementId = "announcement_id1"
            )
        )

        private val fileDocumentList = listOf(
            FileDocument(
                uri = "uri",
                name = "name",
                size = 10000L,
                absolutePath = "absolute_path/name",
                lastModifiedDate = "last_modified_date",
                previewDrawable = R.drawable.ic_pdf
            ),
            FileDocument(
                uri = "uri1",
                name = "name1",
                size = 10000L,
                absolutePath = "absolute_path/name1",
                lastModifiedDate = "last_modified_date",
                previewDrawable = R.drawable.ic_docx
            )
        )
    }
}