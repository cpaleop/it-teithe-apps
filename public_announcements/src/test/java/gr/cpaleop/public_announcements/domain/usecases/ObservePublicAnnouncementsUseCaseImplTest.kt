package gr.cpaleop.public_announcements.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.public_announcements.domain.repositories.AnnouncementsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class ObservePublicAnnouncementsUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var announcementsRepository: AnnouncementsRepository

    private lateinit var observePublicAnnouncementsUseCaseImpl: ObservePublicAnnouncementsUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        observePublicAnnouncementsUseCaseImpl =
            ObservePublicAnnouncementsUseCaseImpl(announcementsRepository)
    }

    /**
     * Filter channel has default value of an empty string
     */
    @Test
    fun `invoke when filter is empty returns correct flow of announcements`() = runBlocking {
        val expected = announcementList
        coEvery { announcementsRepository.getPublicAnnouncementsFlow() } returns announcementListFlow
        val actual = observePublicAnnouncementsUseCaseImpl().first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke when filter is not empty returns correct flow of announcements`() = runBlocking {
        val givenQuery = "title1"
        val expected = listOf(announcementList[1])
        coEvery { announcementsRepository.getPublicAnnouncementsFlow() } returns announcementListFlow
        observePublicAnnouncementsUseCaseImpl.filter(givenQuery)
        val actual = observePublicAnnouncementsUseCaseImpl().first()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke when filter is not empty returns empty flow of announcements`() = runBlocking {
        val givenQuery = "title11"
        val expected = emptyList<Announcement>()
        coEvery { announcementsRepository.getPublicAnnouncementsFlow() } returns announcementListFlow
        observePublicAnnouncementsUseCaseImpl.filter(givenQuery)
        val actual = observePublicAnnouncementsUseCaseImpl().first()
        assertThat(actual).isEqualTo(expected)
    }

    companion object {

        private val announcementListFlow = flow { emit(announcementList) }
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

        /*private val remoteAnnouncementList = listOf(
            RemoteAnnouncement(
                id = "id",
                title = "title",
                text = "text",
                date = "date",
                publisher = RemotePublisher(
                    id = "publisher_id",
                    name = "publisher_name"
                ),
                attachments = emptyList(),
                about = "about_id"
            ),
            RemoteAnnouncement(
                id = "id1",
                title = "title1",
                text = "text1",
                date = "date1",
                publisher = RemotePublisher(
                    id = "publisher_id",
                    name = "publisher_name1"
                ),
                attachments = emptyList(),
                about = "about_id1"
            )
        )*/
    }
}