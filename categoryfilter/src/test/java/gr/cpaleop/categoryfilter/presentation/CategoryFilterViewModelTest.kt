package gr.cpaleop.categoryfilter.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun `presentCategoryName has correct value`() {
        val expectedValue = "category_name"
        coEvery { getCategoryNameUseCase(viewModel.categoryId) } returns expectedValue
        viewModel.presentCategoryName()
        assertThat(LiveDataTest.getValue(viewModel.categoryName)).isEqualTo(expectedValue)
    }

    @Test
    fun `presentCategoryName when fails catches exception`() {
        coEvery { getCategoryNameUseCase(viewModel.categoryId) } throws Throwable()
        viewModel.presentCategoryName()
    }

    @Test
    fun `presentAnnouncements is success`() {
        coEvery { observeAnnouncementsByCategoryUseCase.refresh(viewModel.categoryId) } returns Unit
        viewModel.presentAnnouncements()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `presentAnnouncements when fails catches exception`() {
        coEvery { observeAnnouncementsByCategoryUseCase.refresh(viewModel.categoryId) } throws Throwable()
        viewModel.presentAnnouncements()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
    }

    @Test
    fun `announcements livedata correct values with no filter`() =
        testMainCoroutineDispatcher.runBlockingTest {
            Dispatchers.setMain(testMainCoroutineDispatcher)
            val expectedAnnouncementList = announcementPresentationList
            val expectedFlow = flow {
                emit(announcementList)
            }
            coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow.flowOn(
                testMainCoroutineDispatcher
            )
            every { announcementPresentationMapper(announcementList[0]) } returns announcementPresentationList[0]
            every { announcementPresentationMapper(announcementList[1]) } returns announcementPresentationList[1]

            assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(
                expectedAnnouncementList
            )
            assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(false)
        }

    @Test
    fun `announcements livedata empty list`() = testMainCoroutineDispatcher.runBlockingTest {
        Dispatchers.setMain(testMainCoroutineDispatcher)
        val announcementList = emptyList<Announcement>()
        val expected = emptyList<AnnouncementPresentation>()
        val expectedFlow = flow {
            emit(announcementList)
        }
        coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow
        assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(true)
    }

    @Test
    fun `announcements livedata throws exception`() = testMainCoroutineDispatcher.runBlockingTest {
        val expected = null
        coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } throws Throwable(
            ""
        )
        assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(expected)
    }

    companion object {

        private val announcementPresentationList = listOf(
            AnnouncementPresentation(
                id = "id",
                title = "title",
                content = "text",
                date = "date",
                publisherName = "publisher_name",
                hasAttachments = false,
                category = "category_name"
            ),
            AnnouncementPresentation(
                id = "id1",
                title = "title1",
                content = "text1",
                date = "date1",
                publisherName = "publisher_name1",
                hasAttachments = false,
                category = "category_name1"
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