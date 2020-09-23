package gr.cpaleop.announcement.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.announcement.domain.repositories.AnnouncementsRepository
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetAnnouncementUseCaseImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var announcementsRepository: AnnouncementsRepository

    private lateinit var getAnnouncementUseCase: GetAnnouncementUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        getAnnouncementUseCase = GetAnnouncementUseCaseImpl(announcementsRepository)
    }

    @Test
    fun `invoke returns expected announcement`() = runBlocking {
        val expected = Announcement(
            id = "id",
            attachments = emptyList(),
            publisherName = "publisher_name",
            text = "text",
            title = "title",
            date = "date",
            category = Category(
                id = "category_id",
                name = "category_name"
            )
        )
        val announcementId = "id"
        coEvery { announcementsRepository.getAnnouncementById(announcementId) } returns expected
        val actual = getAnnouncementUseCase(announcementId)
        assertThat(actual).isEqualTo(expected)
    }
}