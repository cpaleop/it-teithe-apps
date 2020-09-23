package gr.cpaleop.categoryfilter.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.categoryfilter.domain.usecases.FilterAnnouncementsUseCase
import gr.cpaleop.categoryfilter.domain.usecases.GetCategoryNameUseCase
import gr.cpaleop.categoryfilter.domain.usecases.ObserveAnnouncementsByCategoryUseCase
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class CategoryFilterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getCategoryNameUseCase: GetCategoryNameUseCase

    @MockK
    private lateinit var observeAnnouncementsByCategoryUseCase: ObserveAnnouncementsByCategoryUseCase

    @MockK
    private lateinit var filterAnnouncementsUseCase: FilterAnnouncementsUseCase

    private lateinit var viewModel: CategoryFilterViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = CategoryFilterViewModel(
            testCoroutineDispatcher,
            getCategoryNameUseCase,
            observeAnnouncementsByCategoryUseCase,
            filterAnnouncementsUseCase
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
    fun `presentCategoryName throws`() {
        coEvery { getCategoryNameUseCase(viewModel.categoryId) } throws Throwable("")
        viewModel.presentCategoryName()
    }

    /**
     * This test case makes use of [TestCoroutineDispatcher.advanceTimeBy] method because the produced flow has a debounce
     */
    @Test
    fun `presentAnnouncementsByCategory emits correct values with no filter`() =
        testCoroutineDispatcher.runBlockingTest {
            val expectedAnnouncementList = announcementList
            val expectedFlow = flow {
                emit(expectedAnnouncementList)
            }

            coEvery {
                filterAnnouncementsUseCase(
                    expectedAnnouncementList,
                    ""
                )
            } returns expectedAnnouncementList
            coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow
            viewModel.presentAnnouncementsByCategory()
            advanceTimeBy(200)
            assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(
                expectedAnnouncementList
            )
            assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(false)
        }

    /**
     * This test case makes use of [TestCoroutineDispatcher.advanceTimeBy] method because the produced flow has a debounce
     */
    @Test
    fun `presentAnnouncementsByCategory emits correct values with filter`() =
        testCoroutineDispatcher.runBlockingTest {
            val originalAnnouncementList = announcementList
            val expectedFilteredAnnouncementList = listOf(announcementList[1])
            val expectedFlow = flow {
                emit(originalAnnouncementList)
            }
            coEvery {
                filterAnnouncementsUseCase(
                    originalAnnouncementList,
                    "title1"
                )
            } returns expectedFilteredAnnouncementList
            coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow
            viewModel.filterAnnouncements("title1")
            viewModel.presentAnnouncementsByCategory()
            advanceTimeBy(200)
            assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(
                expectedFilteredAnnouncementList
            )
            assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(false)
        }

    /**
     * This test case makes use of [TestCoroutineDispatcher.advanceTimeBy] method because the produced flow has a debounce
     */
    @Test
    fun `presentAnnouncementsByCategory emits empty list`() =
        testCoroutineDispatcher.runBlockingTest {
            val emptyAnnouncementList = emptyList<Announcement>()
            val expectedFlow = flow {
                emit(emptyAnnouncementList)
            }
            coEvery {
                filterAnnouncementsUseCase(
                    emptyAnnouncementList,
                    ""
                )
            } returns emptyAnnouncementList
            coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } returns expectedFlow
            viewModel.presentAnnouncementsByCategory()
            advanceTimeBy(200)
            assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(
                emptyAnnouncementList
            )
            assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(true)
        }

    @Test
    fun `presentAnnouncementsByCategory throws exception`() =
        testCoroutineDispatcher.runBlockingTest {
            coEvery { observeAnnouncementsByCategoryUseCase(viewModel.categoryId) } throws Throwable(
                ""
            )
            viewModel.presentAnnouncementsByCategory()
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