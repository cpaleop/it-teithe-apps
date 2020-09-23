package gr.cpaleop.categoryfilter.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FilterAnnouncementsUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    private lateinit var filterAnnouncementsUseCase: FilterAnnouncementsUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        filterAnnouncementsUseCase = FilterAnnouncementsUseCaseImpl(testCoroutineDispatcher)
    }

    @Test
    fun `invoke returns correct announcements when filter is not empty`() = runBlocking {
        val givenAnnouncementList = announcementList
        val query = "title1"
        val expectedAnnouncementList = listOf(givenAnnouncementList[1])
        val actual = filterAnnouncementsUseCase(givenAnnouncementList, query)
        assertThat(actual).isEqualTo(expectedAnnouncementList)
    }

    @Test
    fun `invoke returns correct announcements when filter is empty`() = runBlocking {
        val givenAnnouncementList = announcementList
        val query = ""
        val expectedAnnouncementList = announcementList
        val actual = filterAnnouncementsUseCase(givenAnnouncementList, query)
        assertThat(actual).isEqualTo(expectedAnnouncementList)
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