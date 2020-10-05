package gr.cpaleop.announcement.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.announcement.domain.usecases.GetAnnouncementUseCase
import gr.cpaleop.announcement.domain.usecases.ObserveDownloadNotifierUseCase
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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

    @MockK
    private lateinit var observeDownloadNotifierUseCase: ObserveDownloadNotifierUseCase

    private lateinit var viewModel: AnnouncementViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = AnnouncementViewModel(
            testCoroutineDispatcher,
            getAnnouncementUseCase,
            announcementDetailsMapper,
            observeDownloadNotifierUseCase
        )
    }

    @Test
    fun `presentAnnouncement success`() {
        val announcementId = "_id"
        val expectedValue = announcementDetails
        coEvery { getAnnouncementUseCase(announcementId) } returns announcement
        coEvery { announcementDetailsMapper(announcement) } returns expectedValue
        viewModel.presentAnnouncement(announcementId)
        assertThat(LiveDataTest.getValue(viewModel.announcement)).isEqualTo(expectedValue)
    }

    @Test
    fun `presentAnnouncement catches exception when throws`() {
        val fakeAnnouncementId = "_id"
        coEvery { getAnnouncementUseCase(fakeAnnouncementId) } throws Throwable()
        viewModel.presentAnnouncement(fakeAnnouncementId)
    }

    @Test
    fun `presentAnnouncement and downloadAttachments has correct values`() {
        val announcementId = "_id"
        val announcement = announcementWithAttachments
        val mappedAnnouncement = announcementDetailsWithAttachments
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
    fun `presentAnnouncement downloadStatus is true when already downloading`() {
        val announcementId = "_id"
        val downloadNotifierFlow = flow {
            emit(true)
        }
        coEvery { getAnnouncementUseCase(announcementId) } returns announcement
        coEvery { announcementDetailsMapper(announcement) } returns announcementDetails
        coEvery { observeDownloadNotifierUseCase(announcementId) } returns downloadNotifierFlow
        viewModel.presentAnnouncement(announcementId)
        assertThat(LiveDataTest.getValue(viewModel.downloadStatus)).isEqualTo(true)
    }

    @Test
    fun `presentAnnouncement downloadStatus is false when not downloading`() {
        val announcementId = "_id"
        val downloadNotifierFlow = flow {
            emit(false)
        }
        coEvery { getAnnouncementUseCase(announcementId) } returns announcement
        coEvery { announcementDetailsMapper(announcement) } returns announcementDetails
        coEvery { observeDownloadNotifierUseCase(announcementId) } returns downloadNotifierFlow
        viewModel.presentAnnouncement(announcementId)
        assertThat(LiveDataTest.getValue(viewModel.downloadStatus)).isEqualTo(false)
    }

    @Test
    fun `downloadAttachments catches exception when throws`() {
        val announcementId = "_id"
        coEvery { getAnnouncementUseCase(announcementId) } throws Throwable()
        viewModel.downloadAttachments(announcementId)
    }

    companion object {

        val announcementWithAttachments = Announcement(
            id = "_id",
            attachments = listOf("1"),
            publisherName = "publisher_name",
            text = "text",
            title = "title",
            date = "date",
            category = Category(
                id = "id",
                name = "name",
                isRegistered = false
            )
        )
        val announcementDetailsWithAttachments = AnnouncementDetails(
            title = "title",
            category = "name",
            date = "date",
            text = "text",
            publisherName = "publisher_name",
            attachments = listOf("1"),
            id = "_id"
        )

        val announcement = Announcement(
            id = "_id",
            attachments = emptyList(),
            publisherName = "publisher_name",
            text = "text",
            title = "title",
            date = "date",
            category = Category(
                id = "id",
                name = "name",
                isRegistered = false
            )
        )
        val announcementDetails = AnnouncementDetails(
            title = "title",
            category = "name",
            date = "date",
            text = "text",
            publisherName = "publisher_name",
            attachments = emptyList(),
            id = "_id"
        )
    }
}