package gr.cpaleop.public_announcements.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.public_announcements.domain.usecases.ObservePublicAnnouncementsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PublicAnnouncementsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testMainDispatcher = TestCoroutineDispatcher()

    @MainDispatcher
    private val testDefaultDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var observePublicAnnouncementsUseCase: ObservePublicAnnouncementsUseCase

    @MockK
    private lateinit var announcementPresentationMapper: AnnouncementPresentationMapper

    private lateinit var viewModel: PublicAnnouncementsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = PublicAnnouncementsViewModel(
            testMainDispatcher,
            testDefaultDispatcher,
            observePublicAnnouncementsUseCase,
            announcementPresentationMapper
        )
    }

    @Test
    fun `presentAnnouncements when full list is success`() = runBlocking {
        val expected = announcementPresentationList
        coEvery { observePublicAnnouncementsUseCase() } returns flow { emit(announcementList) }
        coEvery { announcementPresentationMapper(announcementList[0]) } returns announcementPresentationList[0]
        coEvery { announcementPresentationMapper(announcementList[1]) } returns announcementPresentationList[1]
        viewModel.presentAnnouncements()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(false)
    }

    @Test
    fun `presentAnnouncements when empty list is success`() = runBlocking {
        val expected = emptyList<AnnouncementPresentation>()
        coEvery { observePublicAnnouncementsUseCase() } returns flow { emit(emptyList<Announcement>()) }
        viewModel.presentAnnouncements()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.announcements)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.announcementsEmpty)).isEqualTo(true)
    }

    @Test
    fun `presentAnnouncements when failure catches exception`() = runBlocking {
        coEvery { observePublicAnnouncementsUseCase() } throws Throwable()
        viewModel.presentAnnouncements()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
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
    }
}