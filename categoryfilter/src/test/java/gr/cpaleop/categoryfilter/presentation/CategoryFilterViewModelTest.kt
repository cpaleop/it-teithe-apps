package gr.cpaleop.categoryfilter.presentation

import android.text.SpannableString
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common_test.testValue
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.network.connection.NoConnectionException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import gr.cpaleop.teithe_apps.R as appR

@FlowPreview
@ExperimentalCoroutinesApi
class CategoryFilterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testMainCoroutineDispatcher = TestCoroutineDispatcher()

    @DefaultDispatcher
    private val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getCategoryNameUseCase: GetCategoryNameUseCase

    @MockK
    private lateinit var observeAnnouncementsByCategoryUseCase: ObserveAnnouncementsByCategoryUseCase

    @MockK
    private lateinit var announcementPresentationMapper: AnnouncementPresentationMapper

    private lateinit var viewModel: CategoryFilterViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = CategoryFilterViewModel(
            testMainCoroutineDispatcher,
            testDefaultCoroutineDispatcher,
            getCategoryNameUseCase,
            observeAnnouncementsByCategoryUseCase,
            announcementPresentationMapper
        ).apply {
            categoryId = "id"
        }
    }

    @Test
    fun `presentCategoryName has correct value`() = runBlocking {
        val expectedValue = "category_name"
        coEvery { getCategoryNameUseCase(viewModel.categoryId) } returns expectedValue
        viewModel.presentCategoryName()
        assertThat(viewModel.categoryName.testValue).isEqualTo(expectedValue)
    }

    @Test
    fun `presentCategoryName catches exception when failure`() = runBlocking {
        coEvery { getCategoryNameUseCase(viewModel.categoryId) } throws Throwable()
        viewModel.presentCategoryName()
    }

    @Test
    fun `refreshAnnouncements is success`() = runBlocking {
        coEvery { observeAnnouncementsByCategoryUseCase.refresh(viewModel.categoryId) } returns Unit
        viewModel.refreshAnnouncements()
        assertThat(viewModel.loading.testValue).isEqualTo(false)
    }

    @Test
    fun `refreshAnnouncements has correct message when error`() = runBlocking {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { observeAnnouncementsByCategoryUseCase.refresh(viewModel.categoryId) } throws Throwable()
        viewModel.refreshAnnouncements()
        assertThat(viewModel.loading.testValue).isEqualTo(false)
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `refreshAnnouncements has correct message when no internet connection`() = runBlocking {
        val expectedMessage = Message(appR.string.error_no_internet_connection)
        coEvery { observeAnnouncementsByCategoryUseCase.refresh(viewModel.categoryId) } throws NoConnectionException()
        viewModel.refreshAnnouncements()
        assertThat(viewModel.loading.testValue).isEqualTo(false)
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentAnnouncements has correct values with no filter`() = runBlocking {
        val expectedAnnouncementList = announcementPresentationList
        val expectedFlow = flow {
            emit(announcementList)
        }
        coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow.flowOn(
            testMainCoroutineDispatcher
        )
        every { observeAnnouncementsByCategoryUseCase.filter } returns ""
        every { announcementPresentationMapper(announcementList[0]) } returns announcementPresentationList[0]
        every { announcementPresentationMapper(announcementList[1]) } returns announcementPresentationList[1]
        viewModel.presentAnnouncements()
        assertThat(viewModel.announcements.testValue).isEqualTo(expectedAnnouncementList)
        assertThat(viewModel.announcementsEmpty.testValue).isEqualTo(false)
    }

    @Test
    fun `presentAnnouncements empty list`() = runBlocking {
        val announcementList = emptyList<Announcement>()
        val expected = emptyList<AnnouncementPresentation>()
        val expectedFlow = flow { emit(announcementList) }
        coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow
        viewModel.presentAnnouncements()
        assertThat(viewModel.announcements.testValue).isEqualTo(expected)
        assertThat(viewModel.announcementsEmpty.testValue).isEqualTo(true)
    }

    @Test
    fun `presentAnnouncements has correct message when error`() = testMainCoroutineDispatcher.runBlockingTest {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } throws Throwable()
        viewModel.presentAnnouncements()
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    @Test
    fun `filterAnnouncements success`() = runBlocking {
        val givenQuery = "query"
        every { observeAnnouncementsByCategoryUseCase.filter = givenQuery } returns Unit
        viewModel.filterAnnouncements(givenQuery)
    }

    @Test
    fun `filterAnnouncements has correct message when error`() = runBlocking {
        val givenQuery = "query"
        val expectedMessage = Message(appR.string.error_generic)
        every { observeAnnouncementsByCategoryUseCase.filter = givenQuery } throws Throwable()
        viewModel.filterAnnouncements(givenQuery)
        assertThat(viewModel.message.testValue).isEqualTo(expectedMessage)
    }

    companion object {

        private val announcementPresentationList = listOf(
            AnnouncementPresentation(
                id = "id",
                title = SpannableString("title"),
                content = SpannableString("text"),
                date = "date",
                publisherName = SpannableString("publisher_name"),
                hasAttachments = false,
                category = SpannableString("category_name")
            ),
            AnnouncementPresentation(
                id = "id1",
                title = SpannableString("title1"),
                content = SpannableString("text1"),
                date = "date1",
                publisherName = SpannableString("publisher_name1"),
                hasAttachments = false,
                category = SpannableString("category_name1")
            )
        )

        private val announcementList = listOf(
            Announcement(
                id = "id",
                title = "title",
                text = "text",
                date = "date",
                publisherName = "publisher_name",
                attachments = emptyList(),
                category = Category(
                    id = "category_id",
                    name = "category_name",
                    isRegistered = false
                )
            ),
            Announcement(
                id = "id1",
                title = "title1",
                text = "text1",
                date = "date1",
                publisherName = "publisher_name1",
                attachments = emptyList(),
                category = Category(
                    id = "category_id1",
                    name = "category_name1",
                    isRegistered = false
                )
            )
        )
    }
}