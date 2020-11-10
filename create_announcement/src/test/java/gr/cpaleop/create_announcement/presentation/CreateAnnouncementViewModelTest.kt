package gr.cpaleop.create_announcement.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.testValue
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.create_announcement.R
import gr.cpaleop.create_announcement.domain.entities.EmptyCategoryException
import gr.cpaleop.create_announcement.domain.entities.EmptyTextException
import gr.cpaleop.create_announcement.domain.entities.EmptyTitleException
import gr.cpaleop.create_announcement.domain.usecases.GetCategoriesUseCase
import gr.cpaleop.create_announcement.domain.usecases.GetCategoryUseCase
import gr.cpaleop.upload.domain.entities.MultilanguageText
import gr.cpaleop.upload.domain.entities.NewAnnouncement
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import gr.cpaleop.teithe_apps.R as appR

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
    private lateinit var createAnnouncementUseCase: gr.cpaleop.upload.domain.usecases.CreateAnnouncementUseCase

    private lateinit var viewModel: CreateAnnouncementViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = CreateAnnouncementViewModel(
            testMainDispatcher,
            getCategoriesUseCase,
            getCategoryUseCase,
            createAnnouncementUseCase
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
        val expectedResult = Unit
        coEvery { createAnnouncementUseCase(givenNewAnnouncement) } returns Unit
        viewModel.addTitleEn(titleEn)
        viewModel.addTitleGr(titleGr)
        viewModel.addTextEn(textEn)
        viewModel.addTextGr(textGr)
        viewModel.addCategory(category)
        viewModel.addAttachments(attachmentUriList)
        viewModel.createAnnouncement()
        assertThat(viewModel.announcementCreated.testValue).isEqualTo(expectedResult)
    }

    @Test
    fun `createAnnouncement has message when title is empty`() = runBlocking {
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
        coEvery { createAnnouncementUseCase(givenNewAnnouncement) } throws EmptyTitleException()
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
    fun `createAnnouncement has message when text is empty`() = runBlocking {
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
        coEvery { createAnnouncementUseCase(givenNewAnnouncement) } throws EmptyTextException()
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
    fun `createAnnouncement has message when category is empty`() = runBlocking {
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
        coEvery { createAnnouncementUseCase(givenNewAnnouncement) } throws EmptyCategoryException()
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
    fun `presentCategories has message when error`() = runBlocking {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getCategoriesUseCase() } throws Throwable()
        viewModel.presentCategories()
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `selectCategory has correct value`() = runBlocking {
        val givenCategoryId = "id1"
        val expectedCategory = categoryList[0]
        coEvery { getCategoryUseCase(givenCategoryId) } returns categoryList[0]
        viewModel.selectCategory(givenCategoryId)
        assertThat(viewModel.category.testValue).isEqualTo(expectedCategory)
    }

    @Test
    fun `selectCategory has message when error`() = runBlocking {
        val givenCategoryId = "id1"
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { getCategoryUseCase(givenCategoryId) } throws Throwable()
        viewModel.selectCategory(givenCategoryId)
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    companion object {

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
}