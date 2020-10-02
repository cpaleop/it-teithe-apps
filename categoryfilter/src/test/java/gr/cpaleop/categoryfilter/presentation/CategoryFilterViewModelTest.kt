package gr.cpaleop.categoryfilter.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.MainDispatcher
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

    @MockK
    private lateinit var getCategoryNameUseCase: GetCategoryNameUseCase

    @MockK
    private lateinit var observeAnnouncementsByCategoryUseCase: ObserveAnnouncementsByCategoryUseCase

    private lateinit var viewModel: CategoryFilterViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = CategoryFilterViewModel(
            testMainCoroutineDispatcher,
            getCategoryNameUseCase,
            observeAnnouncementsByCategoryUseCase
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
            val expectedAnnouncementList = announcementList
            val expectedFlow = flow {
                emit(expectedAnnouncementList)
            }
            coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow.flowOn(
                testMainCoroutineDispatcher
            )
            assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(
                expectedAnnouncementList
            )
            assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(false)
        }

    @Test
    fun `announcements livedata empty list`() = testMainCoroutineDispatcher.runBlockingTest {
        Dispatchers.setMain(testMainCoroutineDispatcher)
        val emptyAnnouncementList = emptyList<Announcement>()
        val expectedFlow = flow {
            emit(emptyAnnouncementList)
        }
        coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow
        assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(
            emptyAnnouncementList
        )
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

        private val announcementList = listOf(
            Announcement(
                id = "id",
                title = "title",
                text = "text",
                date = "date",
                publisherName = "publisher_name",
                attachments = emptyList()
            ),
            Announcement(
                id = "id1",
                title = "title1",
                text = "text1",
                date = "date1",
                publisherName = "publisher_name1",
                attachments = emptyList()
            )
        )
    }
}