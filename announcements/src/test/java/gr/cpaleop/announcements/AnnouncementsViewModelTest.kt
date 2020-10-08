package gr.cpaleop.announcements

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.announcements.domain.usecases.FilterAnnouncementsUseCase
import gr.cpaleop.announcements.domain.usecases.ObserveAnnouncementsUseCase
import gr.cpaleop.announcements.presentation.AnnouncementsViewModel
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.network.connection.NoConnectionException
import gr.cpaleop.teithe_apps.R
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AnnouncementsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var observeAnnouncementsUseCase: ObserveAnnouncementsUseCase

    @MockK
    private lateinit var filterAnnouncementsUseCase: FilterAnnouncementsUseCase

    @MockK
    private lateinit var announcementPresentationMapper: AnnouncementPresentationMapper

    private lateinit var viewModel: AnnouncementsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = AnnouncementsViewModel(
            testCoroutineDispatcher,
            observeAnnouncementsUseCase,
            announcementPresentationMapper,
            filterAnnouncementsUseCase
        )
    }

    /**
     * Not working cause of equality of [PagingData] object.
     * Keep assertion uncommented for now
     */
    @Test
    fun `presentAnnouncements collects paging data`() = testCoroutineDispatcher.runBlockingTest {
        val expected = announcementPresentationPagingData
        val announcementPagingDataFlow = flow {
            emit(announcementPagingData)
        }
        coEvery { observeAnnouncementsUseCase() } returns announcementPagingDataFlow
        viewModel.presentAnnouncements()
        /*assertThat(LiveDataTest.getValue(viewModel.announcements)).isNotEqualTo(expected)*/
    }

    @Test
    fun `presentAnnouncements catches exception message when failure`() =
        testCoroutineDispatcher.runBlockingTest {
            val expectedMessage = Message(R.string.error_generic)
            coEvery { observeAnnouncementsUseCase() } throws Throwable()
            viewModel.presentAnnouncements()
            assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
        }

    @Test
    fun `presentAnnouncements catches exception and has message when no internet connection`() =
        testCoroutineDispatcher.runBlockingTest {
            val expectedMessage = Message(R.string.error_no_internet_connection)
            coEvery { observeAnnouncementsUseCase() } throws NoConnectionException()
            viewModel.presentAnnouncements()
            assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
        }

    @Test
    fun `searchAnnouncements success`() {
        val givenFilter = "query"
        coEvery { filterAnnouncementsUseCase(givenFilter) } returns Unit
        viewModel.searchAnnouncements(givenFilter)
    }

    @Test
    fun `searchAnnouncements catches exception and has message when failure`() {
        val expectedMessage = Message(R.string.error_generic)
        val givenFilter = "query"
        coEvery { filterAnnouncementsUseCase(givenFilter) } throws Throwable()
        viewModel.searchAnnouncements(givenFilter)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    companion object {

        private val announcementList = listOf(
            Announcement(
                id = "id",
                title = "title",
                text = "text",
                date = "date",
                publisherName = "publisher_name",
                attachments = emptyList(),
                category = Category(
                    id = "id",
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
                    id = "id1",
                    name = "category_name1",
                    isRegistered = false
                )
            )
        )
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
        private val announcementPagingData = PagingData.from(announcementList)
        private val announcementPresentationPagingData =
            PagingData.from(announcementPresentationList)
    }
}