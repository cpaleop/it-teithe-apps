package gr.cpaleop.create_announcement.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.testValue
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.create_announcement.R
import gr.cpaleop.create_announcement.domain.behavior.AnnouncementValidator
import gr.cpaleop.create_announcement.domain.entities.Attachment
import gr.cpaleop.create_announcement.domain.entities.EmptyCategoryException
import gr.cpaleop.create_announcement.domain.entities.EmptyTextException
import gr.cpaleop.create_announcement.domain.entities.EmptyTitleException
import gr.cpaleop.create_announcement.domain.usecases.*
import gr.cpaleop.create_announcement.presentation.attachments.AttachmentPresentation
import gr.cpaleop.create_announcement.presentation.attachments.AttachmentPresentationMapper
import gr.cpaleop.upload.domain.behavior.UploadProgressNotifier
import gr.cpaleop.upload.domain.entities.MultilanguageText
import gr.cpaleop.upload.domain.entities.NewAnnouncement
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import gr.cpaleop.teithe_apps.R as appR

@FlowPreview
@ExperimentalCoroutinesApi
class CreateAnnouncementViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testMainDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @MockK
    private lateinit var getCategoryUseCase: GetCategoryUseCase

    @MockK
    private lateinit var getSelectedAttachmentsUseCase: GetSelectedAttachmentsUseCase

    @MockK
    private lateinit var addAttachmentsUseCase: AddAttachmentsUseCase

    @MockK
    private lateinit var removeAttachmentsUseCase: RemoveAttachmentsUseCase

    @MockK
    private lateinit var announcementValidator: AnnouncementValidator

    @MockK
    private lateinit var attachmentPresentationMapper: AttachmentPresentationMapper

    @MockK
    private lateinit var uploadProgressNotifier: UploadProgressNotifier

    private lateinit var viewModel: CreateAnnouncementViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = CreateAnnouncementViewModel(
            testMainDispatcher,
            getCategoriesUseCase,
            getCategoryUseCase,
            getSelectedAttachmentsUseCase,
            addAttachmentsUseCase,
            removeAttachmentsUseCase,
            announcementValidator,
            attachmentPresentationMapper,
            uploadProgressNotifier
        )
    }

    @Test
    fun `createAnnouncement is successful`() = runBlocking {
        val titleEn = "titleEn"
        val titleGr = "titleGr"
        val textEn = "textEn"
        val textGr = "textGr"
        val category = "id3"
        val attachmentUriList = listOf("uri1", "uri2")
        val givenNewAnnouncement = NewAnnouncement(
            title = MultilanguageText(titleEn, titleGr),
            text = MultilanguageText(textEn, textGr),
            category = category,
            attachmentsUriList = attachmentUriList
        )
        val expectedAnnouncement = givenNewAnnouncement
        coEvery { addAttachmentsUseCase(attachmentUriList) } returns Unit
        coEvery { announcementValidator(givenNewAnnouncement) } returns givenNewAnnouncement
        viewModel.addTitleEn(titleEn)
        viewModel.addTitleGr(titleGr)
        viewModel.addTextEn(textEn)
        viewModel.addTextGr(textGr)
        viewModel.addCategory(category)
        viewModel.addAttachments(attachmentUriList)
        viewModel.createAnnouncement()
        assertThat(viewModel.enqueueAnnouncement.testValue).isEqualTo(expectedAnnouncement)
    }

    @Test
    fun `createAnnouncement has correct message when title is empty`() = runBlocking {
        val titleEn = ""
        val titleGr = ""
        val textEn = "textEn"
        val textGr = "textGr"
        val category = "id3"
        val attachmentUriList = listOf("uri1", "uri2")
        val givenNewAnnouncement = NewAnnouncement(
            title = MultilanguageText(titleEn, titleGr),
            text = MultilanguageText(textEn, textGr),
            category = category,
            attachmentsUriList = attachmentUriList
        )
        val expectedMessage = Message(R.string.create_announcement_error_title_empty)
        coEvery { addAttachmentsUseCase(attachmentUriList) } returns Unit
        coEvery { announcementValidator(givenNewAnnouncement) } throws EmptyTitleException()
        viewModel.addTitleEn(titleEn)
        viewModel.addTitleGr(titleGr)
        viewModel.addTextEn(textEn)
        viewModel.addTextGr(textGr)
        viewModel.addCategory(category)
        viewModel.addAttachments(attachmentUriList)
        viewModel.createAnnouncement()
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `createAnnouncement has correct message when text is empty`() = runBlocking {
        val titleEn = "titleEn"
        val titleGr = "titleGr"
        val textEn = ""
        val textGr = ""
        val category = "id3"
        val attachmentUriList = listOf("uri1", "uri2")
        val givenNewAnnouncement = NewAnnouncement(
            title = MultilanguageText(titleEn, titleGr),
            text = MultilanguageText(textEn, textGr),
            category = category,
            attachmentsUriList = attachmentUriList
        )
        val expectedMessage = Message(R.string.create_announcement_error_text_empty)
        coEvery { addAttachmentsUseCase(attachmentUriList) } returns Unit
        coEvery { announcementValidator(givenNewAnnouncement) } throws EmptyTextException()
        viewModel.addTitleEn(titleEn)
        viewModel.addTitleGr(titleGr)
        viewModel.addTextEn(textEn)
        viewModel.addTextGr(textGr)
        viewModel.addCategory(category)
        viewModel.addAttachments(attachmentUriList)
        viewModel.createAnnouncement()
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `createAnnouncement has correct message when category is empty`() = runBlocking {
        val titleEn = "titleEn"
        val titleGr = "titleGr"
        val textEn = "textEn"
        val textGr = "textGr"
        val category = ""
        val attachmentUriList = listOf("uri1", "uri2")
        val givenNewAnnouncement = NewAnnouncement(
            title = MultilanguageText(titleEn, titleGr),
            text = MultilanguageText(textEn, textGr),
            category = category,
            attachmentsUriList = attachmentUriList
        )
        val expectedMessage = Message(R.string.create_announcement_error_category_empty)
        coEvery { addAttachmentsUseCase(attachmentUriList) } returns Unit
        coEvery { announcementValidator(givenNewAnnouncement) } throws EmptyCategoryException()
        viewModel.addTitleEn(titleEn)
        viewModel.addTitleGr(titleGr)
        viewModel.addTextEn(textEn)
        viewModel.addTextGr(textGr)
        viewModel.addCategory(category)
        viewModel.addAttachments(attachmentUriList)
        viewModel.createAnnouncement()
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentCategories has correct value`() = runBlocking {
        val expectedCategoryList = categoryList
        coEvery { getCategoriesUseCase() } returns categoryList
        viewModel.presentCategories()
        assertThat(viewModel.categories.testValue).isEqualTo(expectedCategoryList)
    }

    @Test
    fun `presentCategories has correct message when error`() = runBlocking {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getCategoriesUseCase() } throws Throwable()
        viewModel.presentCategories()
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `selectCategory has correct value`() = runBlocking {
        val givenCategoryId = "id1"
        val expectedCategory = categoryList[0].name
        coEvery { getCategoryUseCase(givenCategoryId) } returns categoryList[0]
        viewModel.selectCategory(givenCategoryId)
        assertThat(viewModel.category.testValue).isEqualTo(expectedCategory)
    }

    @Test
    fun `selectCategory has correct message when error`() = runBlocking {
        val givenCategoryId = "id1"
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getCategoryUseCase(givenCategoryId) } throws Throwable()
        viewModel.selectCategory(givenCategoryId)
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `addAttachments has correct value`() = runBlocking {
        val givenAttachmentUriList = listOf("uri1", "uri2")
        val expected = attachmentPresentationList
        val expectedCounterValue = "2"
        coEvery { addAttachmentsUseCase(givenAttachmentUriList) } returns Unit
        coEvery { getSelectedAttachmentsUseCase() } returns attachmentList
        coEvery { attachmentPresentationMapper(attachmentList[0].uri) } returns attachmentPresentationList[0]
        coEvery { attachmentPresentationMapper(attachmentList[1].uri) } returns attachmentPresentationList[1]
        viewModel.addAttachments(givenAttachmentUriList)
        assertThat(viewModel.attachments.testValue).isEqualTo(expected)
        assertThat(viewModel.attachmentsEmpty.testValue).isEqualTo(false)
        assertThat(viewModel.attachmentsCounterVisibility.testValue).isEqualTo(true)
        assertThat(viewModel.attachmentsCounter.testValue).isEqualTo(expectedCounterValue)
    }

    @Test
    fun `addAttachments has message when error`() = runBlocking {
        val givenAttachmentUriList = listOf("uri1", "uri2")
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { addAttachmentsUseCase(givenAttachmentUriList) } throws Throwable()
        viewModel.addAttachments(givenAttachmentUriList)
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `removeAttachment has correct value`() = runBlocking {
        val givenUri = "uri1"
        val toBeRemovedAttachmentUriList = listOf(givenUri)
        val expected = attachmentPresentationList
        val expectedCounterValue = "2"
        coEvery { removeAttachmentsUseCase(toBeRemovedAttachmentUriList) } returns Unit
        coEvery { getSelectedAttachmentsUseCase() } returns attachmentList
        coEvery { attachmentPresentationMapper(attachmentList[0].uri) } returns attachmentPresentationList[0]
        coEvery { attachmentPresentationMapper(attachmentList[1].uri) } returns attachmentPresentationList[1]
        viewModel.removeAttachment(givenUri)
        assertThat(viewModel.attachments.testValue).isEqualTo(expected)
        assertThat(viewModel.attachmentsEmpty.testValue).isEqualTo(false)
        assertThat(viewModel.attachmentsCounterVisibility.testValue).isEqualTo(true)
        assertThat(viewModel.attachmentsCounter.testValue).isEqualTo(expectedCounterValue)
    }

    @Test
    fun `removeAttachment has correct message when error`() = runBlocking {
        val givenUri = "uri1"
        val toBeRemovedAttachmentUriList = listOf(givenUri)
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { removeAttachmentsUseCase(toBeRemovedAttachmentUriList) } throws Throwable()
        viewModel.removeAttachment(givenUri)
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentAttachments has correct value`() {
        val expectedList = attachmentPresentationList
        val expectedCounterValue = "2"
        coEvery { getSelectedAttachmentsUseCase() } returns attachmentList
        coEvery { attachmentPresentationMapper(attachmentList[0].uri) } returns attachmentPresentationList[0]
        coEvery { attachmentPresentationMapper(attachmentList[1].uri) } returns attachmentPresentationList[1]
        viewModel.presentAttachments()
        assertThat(viewModel.attachments.testValue).isEqualTo(expectedList)
        assertThat(viewModel.attachmentsEmpty.testValue).isEqualTo(false)
        assertThat(viewModel.attachmentsCounterVisibility.testValue).isEqualTo(true)
        assertThat(viewModel.attachmentsCounter.testValue).isEqualTo(expectedCounterValue)
    }

    @Test
    fun `presentAttachments has correct message when error`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getSelectedAttachmentsUseCase() } throws Throwable()
        viewModel.presentAttachments()
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    companion object

    private

    val attachmentPresentationList = listOf(
        AttachmentPresentation(
            uri = "uri1",
            name = "name",
            typeDrawableRes = appR.drawable.ic_pdf
        ),
        AttachmentPresentation(
            uri = "uri2",
            name = "name",
            typeDrawableRes = appR.drawable.ic_pdf
        )
    )

    private val attachmentList = listOf(
        Attachment(
            uri = "uri1",
            name = "name",
            type = "application/pdf"
        ),
        Attachment(
            uri = "uri2",
            name = "name",
            type = "application/pdf"
        )
    )

    private val categoryList = listOf(
        Category(
            name = "name1",
            id = "id1",
            isRegistered = false
        ),
        Category(
            name = "name2",
            id = "id2",
            isRegistered = false
        )
    )
}