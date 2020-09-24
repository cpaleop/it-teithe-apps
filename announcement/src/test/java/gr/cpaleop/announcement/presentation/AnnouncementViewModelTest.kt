package gr.cpaleop.announcement.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AnnouncementViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getAnnouncementUseCase: GetAnnouncementUseCase

    @MockK
    private lateinit var announcementDetailsMapper: AnnouncementDetailsMapper

    private lateinit var viewModel: AnnouncementViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = AnnouncementViewModel(
            testCoroutineDispatcher,
            getAnnouncementUseCase,
            announcementDetailsMapper
        )
    }

    @Test
    fun `presentAnnouncement success`() {
        val announcementId = "_id"
        val announcement = Announcement(
            id = announcementId,
            attachments = emptyList(),
            publisherName = "publisher_name",
            text = "text",
            title = "title",
            date = "date",
            category = Category(
                id = "id",
                name = "name"
            )
        )
        val expectedValue = AnnouncementDetails(
            title = "title",
            category = "name",
            date = "date",
            text = "text",
            publisherName = "publisher_name",
            attachments = emptyList(),
            id = announcementId
        )
        coEvery { getAnnouncementUseCase(announcementId) } returns announcement
        coEvery { announcementDetailsMapper(announcement) } returns expectedValue
        viewModel.presentAnnouncement(announcementId)
        assertThat(LiveDataTest.getValue(viewModel.announcement)).isEqualTo(expectedValue)
    }

    @Test
    fun `presentAnnouncement when fails catches exception`() {
        val fakeAnnouncementId = "_id"
        coEvery { getAnnouncementUseCase(fakeAnnouncementId) } throws Throwable()
        viewModel.presentAnnouncement(fakeAnnouncementId)
    }

    @Test
    fun `presentAnnouncement and downloadAttachments has correct values`() {
        val announcementId = "_id"
        val announcement = Announcement(
            id = announcementId,
            attachments = listOf("1"),
            publisherName = "publisher_name",
            text = "text",
            title = "title",
            date = "date",
            category = Category(
                id = "id",
                name = "name"
            )
        )
        val mappedAnnouncement = AnnouncementDetails(
            title = "title",
            category = "name",
            date = "date",
            text = "text",
            publisherName = "publisher_name",
            attachments = listOf("1"),
            id = announcementId
        )
        val expectedValue = AnnouncementDocument(
            announcementId = announcementId,
            fileIdList = listOf("1")
        )
        coEvery { getAnnouncementUseCase(announcementId) } returns announcement
        coEvery { announcementDetailsMapper(announcement) } returns mappedAnnouncement
        viewModel.downloadAttachments(announcementId)
        assertThat(LiveDataTest.getValue(viewModel.attachmentFileId)).isEqualTo(expectedValue)
    }

    @Test
    fun `downloadAttachments when fails catches exception`() {
        val announcementId = "_id"
        coEvery { getAnnouncementUseCase(announcementId) } throws Throwable()
        viewModel.downloadAttachments(announcementId)
    }
}