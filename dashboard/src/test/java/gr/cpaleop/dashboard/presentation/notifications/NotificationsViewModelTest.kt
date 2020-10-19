package gr.cpaleop.dashboard.presentation.notifications

import android.text.SpannableString
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.entities.NotificationRelatedAnnouncement
import gr.cpaleop.dashboard.domain.usecases.ObserveNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.ReadAllNotificationsUseCase
import gr.cpaleop.network.connection.NoConnectionException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
class NotificationsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testMainCoroutineDispatcher = TestCoroutineDispatcher()

    @DefaultDispatcher
    private val testDefaultCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var observeNotificationsUseCase: ObserveNotificationsUseCase

    @MockK
    private lateinit var readAllNotificationsUseCase: ReadAllNotificationsUseCase

    @MockK
    private lateinit var notificationPresentationMapper: NotificationPresentationMapper

    private lateinit var viewModel: NotificationsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = NotificationsViewModel(
            testMainCoroutineDispatcher,
            testDefaultCoroutineDispatcher,
            observeNotificationsUseCase,
            readAllNotificationsUseCase,
            notificationPresentationMapper
        )
    }

    @Test
    fun `presentNotifications notifications not empty`() {
        val expectedList = notificationsPresentationList
        val expectedIsEmpty = false
        coEvery { observeNotificationsUseCase() } returns notificationsListFlow
        every { notificationPresentationMapper(notificationsList[0]) } returns notificationsPresentationList[0]
        every { notificationPresentationMapper(notificationsList[1]) } returns notificationsPresentationList[1]
        every { observeNotificationsUseCase.filterStream.value } returns ""
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.notificationsEmpty)).isEqualTo(expectedIsEmpty)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(expectedList)
        assertThat(LiveDataTest.getValue(viewModel.notificationsCounter)).isEqualTo(1)
    }

    @Test
    fun `presentNotifications notifications empty`() {
        val expected = true
        coEvery { observeNotificationsUseCase() } returns notificationsEmptyListFlow
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.notificationsEmpty)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            notificationsEmptyList
        )
        assertThat(LiveDataTest.getValue(viewModel.notificationsCounter)).isEqualTo(0)
    }

    @Test
    fun `presentNotifications catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { observeNotificationsUseCase() } throws Throwable()
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentNotifications catches exception and has message when no internet connection`() {
        val expectedMessage = Message(appR.string.error_no_internet_connection)
        coEvery { observeNotificationsUseCase() } throws NoConnectionException()
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `readAllNotifications success`() {
        coEvery { readAllNotificationsUseCase() } returns Unit
        viewModel.readAllNotifications()
    }

    @Test
    fun `readAllNotifications catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { readAllNotificationsUseCase() } throws Throwable()
        viewModel.readAllNotifications()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `readAllNotifications catches exception and has message when no internet connection`() {
        val expectedMessage = Message(appR.string.error_no_internet_connection)
        coEvery { readAllNotificationsUseCase() } throws NoConnectionException()
        viewModel.readAllNotifications()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `showMessage correct value`() = runBlocking {
        val givenMessage = Message(appR.string.error_generic)
        val expected = Message(appR.string.error_generic)
        viewModel.showMessage(givenMessage)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expected)
    }

    companion object {

        private val notificationsEmptyList = emptyList<Notification>()
        private val notificationsEmptyListFlow = flow {
            emit(notificationsEmptyList)
        }

        private val notificationsList = listOf(
            Notification(
                id = "id1",
                announcement = NotificationRelatedAnnouncement(
                    id = "id1",
                    category = "category1",
                    date = "some_date",
                    title = "title1",
                    publisherName = "publisher_name"
                ),
                seen = true
            ),
            Notification(
                id = "id2",
                announcement = NotificationRelatedAnnouncement(
                    id = "id2",
                    category = "category2",
                    date = "some_date",
                    title = "title2",
                    publisherName = "publisher_name"
                ),
                seen = false
            )
        )
        private val notificationsListFlow = flow {
            emit(notificationsList)
        }

        private val notificationsPresentationList = listOf(
            NotificationPresentation(
                id = "id1",
                category = "category1",
                title = SpannableString("title1"),
                publisherName = SpannableString("publisher_name"),
                date = "date1",
                seen = true
            ),
            NotificationPresentation(
                id = "id2",
                category = "category2",
                title = SpannableString("title2"),
                publisherName = SpannableString("publisher_name"),
                date = "date2",
                seen = false
            )
        )
    }
}