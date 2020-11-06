package gr.cpaleop.favorites.domain.usecases

import com.google.common.truth.Truth.assertThat
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.favorites.domain.repositories.AnnouncementsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ObserveFavoriteAnnouncementsUseCaseImplTest {

    @MockK
    private lateinit var announcementsRepository: AnnouncementsRepository

    private lateinit var observeFavoriteAnnouncementsUseCaseImpl: ObserveFavoriteAnnouncementsUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        observeFavoriteAnnouncementsUseCaseImpl =
            ObserveFavoriteAnnouncementsUseCaseImpl(announcementsRepository)
    }

    @Test
    fun `returns correct values when no filter`() = runBlocking {
        val givenFilter = ""
        val expectedAnnouncementList = announcementList
        coEvery { announcementsRepository.getFavoritesFlow() } returns flowOf(announcementList)
        observeFavoriteAnnouncementsUseCaseImpl.filter = givenFilter
        val actual = observeFavoriteAnnouncementsUseCaseImpl().first()
        assertThat(actual).isEqualTo(expectedAnnouncementList)
    }

    @Test
    fun `returns correct values when there is filter`() = runBlocking {
        val givenFilter = "1"
        val expectedAnnouncementList = listOf(announcementList[1])
        coEvery { announcementsRepository.getFavoritesFlow() } returns flowOf(announcementList)
        observeFavoriteAnnouncementsUseCaseImpl.filter = givenFilter
        val actual = observeFavoriteAnnouncementsUseCaseImpl().first()
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