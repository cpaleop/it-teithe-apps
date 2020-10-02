package gr.cpaleop.documents.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.documents.R
import gr.cpaleop.documents.presentation.document.FileDocument
import gr.cpaleop.documents.presentation.document.FileDocumentMapper
import gr.cpaleop.documents.presentation.options.DocumentDetails
import gr.cpaleop.documents.presentation.options.DocumentOption
import gr.cpaleop.documents.presentation.options.DocumentOptionMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import gr.cpaleop.teithe_apps.R as appR

/**
 * Notice:
 * [DocumentsViewModel.handleDocumentOptionChoice] for type SHARE needs to be Instrumentation tested separately
 *
 * Also, we do not test [DocumentsViewModel.filter] function cause it's logic its tight with [ObserveDocumentsUseCase] and [ObserveDocumentsAnnouncementFoldersUseCase].
 * Prefer to test usecases separately for this functionality
 */
//TODO: Test Announcement folder preview
@ExperimentalCoroutinesApi
class DocumentsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    val testMainCoroutineDispatcher = TestCoroutineDispatcher()

    @DefaultDispatcher
    val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var observeDocumentsUseCase: gr.cpaleop.documents.domain.usecases.ObserveDocumentsUseCase

    @MockK
    private lateinit var fileDocumentMapper: FileDocumentMapper

    @MockK
    private lateinit var getDocumentOptionsUseCase: gr.cpaleop.documents.domain.usecases.GetDocumentOptionsUseCase

    @MockK
    private lateinit var documentOptionMapper: DocumentOptionMapper

    @MockK
    private lateinit var getDocumentUseCase: gr.cpaleop.documents.domain.usecases.GetDocumentUseCase

    @MockK
    private lateinit var deleteDocumentUseCase: gr.cpaleop.documents.domain.usecases.DeleteDocumentUseCase

    @MockK
    private lateinit var renameDocumentUseCase: gr.cpaleop.documents.domain.usecases.RenameDocumentUseCase

    @MockK
    private lateinit var documentSortOptionMapper: gr.cpaleop.documents.presentation.sort.DocumentSortOptionMapper

    @MockK
    private lateinit var observeDocumentsAnnouncementFoldersUseCase: gr.cpaleop.documents.domain.usecases.ObserveDocumentsAnnouncementFoldersUseCase

    @MockK
    private lateinit var observeDocumentSortUseCase: gr.cpaleop.documents.domain.usecases.ObserveDocumentSortUseCase

    @MockK
    private lateinit var getDocumentPreviewPreferenceUseCase: gr.cpaleop.documents.domain.usecases.GetDocumentPreviewPreferenceUseCase

    @MockK
    private lateinit var toggleDocumentPreviewPreferenceUseCase: gr.cpaleop.documents.domain.usecases.ToggleDocumentPreviewPreferenceUseCase

    private lateinit var viewModel: gr.cpaleop.documents.presentation.DocumentsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = gr.cpaleop.documents.presentation.DocumentsViewModel(
            testMainCoroutineDispatcher,
            testDefaultCoroutineDispatcher,
            observeDocumentsUseCase,
            fileDocumentMapper,
            getDocumentOptionsUseCase,
            documentOptionMapper,
            getDocumentUseCase,
            deleteDocumentUseCase,
            renameDocumentUseCase,
            documentSortOptionMapper,
            observeDocumentSortUseCase,
            observeDocumentsAnnouncementFoldersUseCase,
            getDocumentPreviewPreferenceUseCase,
            toggleDocumentPreviewPreferenceUseCase
        )
    }

    @Test
    fun `presentDocuments success with no empty list`() {
        val expected = fileDocumentList
        val documentListFlow = flow {
            emit(documentList)
        }
        coEvery { observeDocumentsUseCase(null) } returns documentListFlow
        coEvery { getDocumentPreviewPreferenceUseCase(null) } returns gr.cpaleop.documents.domain.entities.DocumentPreview.FILE
        coEvery { fileDocumentMapper(documentList[0]) } returns fileDocumentList[0]
        coEvery { fileDocumentMapper(documentList[1]) } returns fileDocumentList[1]
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsEmpty)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentDocuments success with empty list`() {
        val expected = emptyList<FileDocument>()
        val emptyDocumentListFlow = flow<List<Document>> { emit(emptyList()) }
        coEvery { getDocumentPreviewPreferenceUseCase(null) } returns gr.cpaleop.documents.domain.entities.DocumentPreview.FILE
        coEvery { observeDocumentsUseCase(null) } returns emptyDocumentListFlow
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsEmpty)).isEqualTo(true)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentDocuments when fails catches exception`() {
        coEvery { observeDocumentsUseCase(null) } throws Throwable()
        coEvery { getDocumentPreviewPreferenceUseCase(null) } returns gr.cpaleop.documents.domain.entities.DocumentPreview.FILE
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
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
        viewModel.handleDocumentOptionChoice(gr.cpaleop.documents.domain.entities.DocumentOptionType.ANNOUNCEMENT)
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
        viewModel.handleDocumentOptionChoice(gr.cpaleop.documents.domain.entities.DocumentOptionType.RENAME)
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
        viewModel.handleDocumentOptionChoice(gr.cpaleop.documents.domain.entities.DocumentOptionType.DELETE)
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
        val documentSortFlow = flow { emit(selectedDocumentSort) }
        coEvery { observeDocumentSortUseCase() } returns documentSortFlow
        every { documentSortOptionMapper(selectedDocumentSort) } returns selectedDocumentSortOption
        viewModel.presentDocumentSortSelected()
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptionSelected)).isEqualTo(expected)
    }

    @Test
    fun `presentDocumentSortSelected when fails catches exception`() {
        coEvery { observeDocumentSortUseCase() } throws Throwable()
        viewModel.presentDocumentSortSelected()
    }

    @Test
    fun `togglePreview when type is FILE and becomes FOLDER is successful`() {
        val expected = gr.cpaleop.documents.domain.entities.DocumentPreview.FOLDER
        coEvery { toggleDocumentPreviewPreferenceUseCase() } returns gr.cpaleop.documents.domain.entities.DocumentPreview.FOLDER
        viewModel.togglePreview()
        assertThat(LiveDataTest.getValue(viewModel.documentPreview)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.refresh)).isEqualTo(Unit)
    }

    @Test
    fun `togglePreview when type is FOLDER and becomes FILE is successful`() {
        val expected = gr.cpaleop.documents.domain.entities.DocumentPreview.FILE
        coEvery { toggleDocumentPreviewPreferenceUseCase() } returns gr.cpaleop.documents.domain.entities.DocumentPreview.FILE
        viewModel.togglePreview()
        assertThat(LiveDataTest.getValue(viewModel.documentPreview)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.refresh)).isEqualTo(Unit)
    }

    @Test
    fun `togglePreview catches exception`() {
        coEvery { toggleDocumentPreviewPreferenceUseCase() } throws Throwable()
        viewModel.togglePreview()
    }

    companion object {

        private val selectedDocumentSortOption =
            gr.cpaleop.documents.presentation.sort.DocumentSortOption(
                type = gr.cpaleop.documents.domain.entities.DocumentSortType.DATE,
                imageResource = R.drawable.ic_arrow_down,
                labelResource = R.string.documents_sort_date,
                selected = true,
                descending = true
            )

        private val selectedDocumentSort = gr.cpaleop.documents.domain.entities.DocumentSort(
            type = gr.cpaleop.documents.domain.entities.DocumentSortType.DATE,
            selected = true,
            descending = true
        )

        private val documentOptionList = listOf(
            DocumentOption(
                type = gr.cpaleop.documents.domain.entities.DocumentOptionType.ANNOUNCEMENT,
                name = R.string.documents_option_announcement,
                iconResource = R.drawable.ic_link
            ),
            DocumentOption(
                type = gr.cpaleop.documents.domain.entities.DocumentOptionType.RENAME,
                name = R.string.documents_option_rename,
                iconResource = appR.drawable.ic_edit
            ),
            DocumentOption(
                type = gr.cpaleop.documents.domain.entities.DocumentOptionType.DELETE,
                name = R.string.documents_option_delete,
                iconResource = R.drawable.ic_delete
            ),
            DocumentOption(
                type = gr.cpaleop.documents.domain.entities.DocumentOptionType.SHARE,
                name = R.string.documents_option_share,
                iconResource = R.drawable.ic_share
            )
        )

        private val documentOptionTypeList = listOf(
            gr.cpaleop.documents.domain.entities.DocumentOptionType.ANNOUNCEMENT,
            gr.cpaleop.documents.domain.entities.DocumentOptionType.RENAME,
            gr.cpaleop.documents.domain.entities.DocumentOptionType.DELETE,
            gr.cpaleop.documents.domain.entities.DocumentOptionType.SHARE
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
                type = "doc",
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