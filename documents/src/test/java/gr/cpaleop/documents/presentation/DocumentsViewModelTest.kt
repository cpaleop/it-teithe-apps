package gr.cpaleop.documents.presentation

import android.text.SpannableString
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.core.domain.entities.DocumentSort
import gr.cpaleop.core.domain.entities.DocumentSortType
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.documents.R
import gr.cpaleop.documents.domain.FilterStream
import gr.cpaleop.documents.domain.entities.DocumentOptionType
import gr.cpaleop.documents.domain.usecases.*
import gr.cpaleop.documents.presentation.announcement_folder.AnnouncementFolderPresentationMapper
import gr.cpaleop.documents.presentation.document.FileDocument
import gr.cpaleop.documents.presentation.document.FileDocumentMapper
import gr.cpaleop.documents.presentation.document.LastModified
import gr.cpaleop.documents.presentation.options.DocumentDetails
import gr.cpaleop.documents.presentation.options.DocumentOption
import gr.cpaleop.documents.presentation.options.DocumentOptionMapper
import gr.cpaleop.documents.presentation.sort.DocumentSortOption
import gr.cpaleop.documents.presentation.sort.DocumentSortOptionMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CancellationException
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
@ExperimentalCoroutinesApi
class DocumentsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    val testMainCoroutineDispatcher = TestCoroutineDispatcher()

    @DefaultDispatcher
    val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var observeDocumentsUseCase: ObserveDocumentsUseCase

    @MockK
    private lateinit var fileDocumentMapper: FileDocumentMapper

    @MockK
    private lateinit var getDocumentOptionsUseCase: GetDocumentOptionsUseCase

    @MockK
    private lateinit var documentOptionMapper: DocumentOptionMapper

    @MockK
    private lateinit var getDocumentUseCase: GetDocumentUseCase

    @MockK
    private lateinit var deleteDocumentsUseCase: DeleteDocumentsUseCase

    @MockK
    private lateinit var renameDocumentUseCase: RenameDocumentUseCase

    @MockK
    private lateinit var documentSortOptionMapper: DocumentSortOptionMapper

    @MockK
    private lateinit var observeDocumentsAnnouncementFoldersUseCase: ObserveDocumentsAnnouncementFoldersUseCase

    @MockK
    private lateinit var announcementFolderPresentationMapper: AnnouncementFolderPresentationMapper

    @MockK
    private lateinit var observeDocumentSortUseCase: ObserveDocumentSortUseCase

    @MockK
    private lateinit var observeDocumentPreviewPreferenceUseCase: ObserveDocumentPreviewPreferenceUseCase

    @MockK
    private lateinit var toggleDocumentPreviewPreferenceUseCase: ToggleDocumentPreviewPreferenceUseCase

    @MockK
    private lateinit var filterStream: FilterStream

    private lateinit var viewModel: DocumentsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = DocumentsViewModel(
            testMainCoroutineDispatcher,
            testDefaultCoroutineDispatcher,
            observeDocumentsUseCase,
            fileDocumentMapper,
            getDocumentOptionsUseCase,
            documentOptionMapper,
            getDocumentUseCase,
            deleteDocumentsUseCase,
            renameDocumentUseCase,
            documentSortOptionMapper,
            observeDocumentSortUseCase,
            observeDocumentsAnnouncementFoldersUseCase,
            announcementFolderPresentationMapper,
            observeDocumentPreviewPreferenceUseCase,
            toggleDocumentPreviewPreferenceUseCase,
            filterStream
        )
    }

    @Test
    fun `presentDocuments success with no empty list`() {
        val expected = fileDocumentList
        val documentListFlow = flow {
            emit(documentList)
        }
        coEvery { observeDocumentsUseCase(null) } returns documentListFlow
        every { observeDocumentPreviewPreferenceUseCase(null) } returns flow {
            emit(
                DocumentPreview.FILE
            )
        }
        coEvery { fileDocumentMapper(documentList[0]) } returns fileDocumentList[0]
        coEvery { fileDocumentMapper(documentList[1]) } returns fileDocumentList[1]
        every { filterStream.value } returns ""
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsEmpty)).isEqualTo(false)
    }

    @Test
    fun `presentDocuments success with empty list`() {
        val expected = emptyList<FileDocument>()
        val emptyDocumentListFlow = flow<List<Document>> { emit(emptyList()) }
        every { observeDocumentPreviewPreferenceUseCase(null) } returns FLOW_PREVIEW_FILE
        coEvery { observeDocumentsUseCase(null) } returns emptyDocumentListFlow
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.documents)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentsEmpty)).isEqualTo(true)
    }

    @Test
    fun `presentDocuments catches exception and has message when failure while observing documents`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { observeDocumentsUseCase(null) } throws Throwable()
        every { observeDocumentPreviewPreferenceUseCase(null) } returns FLOW_PREVIEW_FILE
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentDocuments catches cancellation exception and has no message when failure while observing documents`() {
        val expectedMessage = null
        coEvery { observeDocumentsUseCase(null) } throws CancellationException()
        every { observeDocumentPreviewPreferenceUseCase(null) } returns FLOW_PREVIEW_FILE
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentDocuments catches exception and has message when failure while observing announcement folders`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { observeDocumentsAnnouncementFoldersUseCase() } throws Throwable()
        every { observeDocumentPreviewPreferenceUseCase(null) } returns FLOW_PREVIEW_FOLDER
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentDocuments catches cancellation exception and has no message when failure while observing announcement folders`() {
        val expectedMessage = null
        coEvery { observeDocumentsAnnouncementFoldersUseCase() } throws CancellationException()
        every { observeDocumentPreviewPreferenceUseCase(null) } returns FLOW_PREVIEW_FOLDER
        viewModel.presentDocuments(null)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
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
    fun `presentDocumentDetails catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        val givenUri = "uri"
        coEvery { getDocumentUseCase(givenUri) } throws Throwable()
        viewModel.presentDocumentDetails(givenUri)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
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
    fun `presentDocumentOptions catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getDocumentOptionsUseCase() } throws Throwable()
        viewModel.presentDocumentOptions()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
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

        val expected = DocumentDetails(uriList = listOf(document.uri), name = document.name)
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

        val expected = DocumentDetails(uriList = listOf(document.uri), name = document.name)
        viewModel.handleDocumentOptionChoice(DocumentOptionType.DELETE)
        assertThat(LiveDataTest.getValue(viewModel.optionDelete)).isEqualTo(expected)
    }

    @Test
    fun `deleteDocument success`() {
        val expectedMessage = Message(R.string.documents_delete_success_message)
        val givenUri = "uri"
        coEvery { deleteDocumentsUseCase(listOf(givenUri)) } returns Unit
        viewModel.deleteDocuments(listOf(givenUri))
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `deleteDocument catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        val givenUri = "uri"
        coEvery { deleteDocumentsUseCase(listOf(givenUri)) } throws Throwable()
        viewModel.deleteDocuments(listOf(givenUri))
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `renameDocument success`() {
        val givenUri = "uri"
        val givenNewName = "newName"
        val expectedMessage = Message(R.string.documents_rename_success_message, givenNewName)
        coEvery { renameDocumentUseCase(givenUri, givenNewName) } returns Unit
        viewModel.renameDocument(givenUri, givenNewName)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `renameDocument catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        val givenUri = "uri"
        val givenNewName = "newName"
        coEvery { renameDocumentUseCase(givenUri, givenNewName) } throws Throwable()
        viewModel.renameDocument(givenUri, givenNewName)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
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
    fun `presentDocumentSortSelected catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { observeDocumentSortUseCase() } throws Throwable()
        viewModel.presentDocumentSortSelected()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `togglePreview when type is FILE and becomes FOLDER is successful`() {
        val expected = DocumentPreview.FOLDER
        coEvery { toggleDocumentPreviewPreferenceUseCase() } returns Unit
        every { observeDocumentPreviewPreferenceUseCase(null) } returns FLOW_PREVIEW_FOLDER
        viewModel.presentDocuments(null)
        viewModel.togglePreview()
        assertThat(LiveDataTest.getValue(viewModel.documentPreview)).isEqualTo(expected)
    }

    @Test
    fun `togglePreview when type is FOLDER and becomes FILE is successful`() {
        val expected = DocumentPreview.FILE
        coEvery { toggleDocumentPreviewPreferenceUseCase() } returns Unit
        every { observeDocumentPreviewPreferenceUseCase(null) } returns FLOW_PREVIEW_FILE
        viewModel.presentDocuments(null)
        viewModel.togglePreview()
        assertThat(LiveDataTest.getValue(viewModel.documentPreview)).isEqualTo(expected)
    }

    @Test
    fun `togglePreview catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { toggleDocumentPreviewPreferenceUseCase() } throws Throwable()
        viewModel.togglePreview()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    companion object {

        private val FLOW_PREVIEW_FILE = flow { emit(DocumentPreview.FILE) }
        private val FLOW_PREVIEW_FOLDER = flow { emit(DocumentPreview.FOLDER) }

        private val selectedDocumentSortOption =
            DocumentSortOption(
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
                iconResource = appR.drawable.ic_edit
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
                type = "doc",
                lastModified = 10000L,
                previewUri = "preview_uri1",
                announcementId = "announcement_id1"
            )
        )

        private val fileDocumentList = listOf(
            FileDocument(
                uri = "uri",
                name = SpannableString("name"),
                size = 10000L,
                absolutePath = "absolute_path/name",
                lastModifiedDate = LastModified(R.string.documents_modified, "last_modified_date"),
                previewDrawable = appR.drawable.ic_pdf
            ),
            FileDocument(
                uri = "uri1",
                name = SpannableString("name1"),
                size = 10000L,
                absolutePath = "absolute_path/name1",
                lastModifiedDate = LastModified(R.string.documents_modified, "last_modified_date"),
                previewDrawable = appR.drawable.ic_docx
            )
        )
    }
}