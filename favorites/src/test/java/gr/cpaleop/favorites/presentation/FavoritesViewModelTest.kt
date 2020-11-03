package gr.cpaleop.favorites.presentation

import android.text.SpannableString
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.testValue
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.domain.entities.Announcement
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.mappers.AnnouncementPresentationMapper
import gr.cpaleop.favorites.domain.usecases.ObserveFavoriteAnnouncementsUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testMainDispatcher = TestCoroutineDispatcher()

    @DefaultDispatcher
    private val testDefaultDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var observeFavoriteAnnouncementsUseCase: ObserveFavoriteAnnouncementsUseCase

    @MockK
    private lateinit var announcementPresentationMapper: AnnouncementPresentationMapper

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = FavoritesViewModel(
            testMainDispatcher,
            testDefaultDispatcher,
            announcementPresentationMapper,
            observeFavoriteAnnouncementsUseCase
        )
    }

    @Test
    fun `showsCorrectItems when no filter`() = runBlocking {
        val expectedAnnouncementList = announcementPresentationList
        every { observeFavoriteAnnouncementsUseCase() } returns flow {
            emit(announcementList)
        }
        every { announcementPresentationMapper(announcementList[0]) } returns announcementPresentationList[0]
        every { announcementPresentationMapper(announcementList[1]) } returns announcementPresentationList[1]
        viewModel.presentAnnouncements()
        assertThat(viewModel.announcements.testValue).isEqualTo(expectedAnnouncementList)
    }

    companion object {

        private val announcementPresentationList = listOf(
            AnnouncementPresentation(
                id = "id",
                title = SpannableString("title"),
                content = SpannableString("text"),
                date = "date",
                publisherName = SpannableString("publisher_name"),
                hasAttachments = false,
                category = SpannableString("category_name")
            ),
            AnnouncementPresentation(
                id = "id1",
                title = SpannableString("title1"),
                content = SpannableString("text1"),
                date = "date1",
                publisherName = SpannableString("publisher_name1"),
                hasAttachments = false,
                category = SpannableString("category_name1")
            )
        )

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