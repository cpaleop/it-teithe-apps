package gr.cpaleop.dashboard.presentation.notifications

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.entities.NotificationRelatedAnnouncement
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.ReadAllNotificationsUseCase
import gr.cpaleop.network.connection.NoConnectionException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var getNotificationsUseCase: GetNotificationsUseCase

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
            getNotificationsUseCase,
            readAllNotificationsUseCase,
            notificationPresentationMapper
        )
    }

    @Test
    fun `presentNotifications notifications not empty`() {
        val expectedList = notificationsPresentationList
        val expectedIsEmpty = false
        coEvery { getNotificationsUseCase() } returns notificationsList
        every { notificationPresentationMapper(notificationsList[0]) } returns notificationsPresentationList[0]
        every { notificationPresentationMapper(notificationsList[1]) } returns notificationsPresentationList[1]
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.notificationsEmpty)).isEqualTo(expectedIsEmpty)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(expectedList)
        assertThat(LiveDataTest.getValue(viewModel.notificationsCounter)).isEqualTo(1)
    }

    @Test
    fun `presentNotifications notifications empty`() {
        val expected = true
        coEvery { getNotificationsUseCase() } returns notificationsEmptyList
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
        coEvery { getNotificationsUseCase() } throws Throwable()
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }

    @Test
    fun `presentNotifications catches exception and has message when no internet connection`() {
        val expectedMessage = Message(appR.string.error_no_internet_connection)
        coEvery { getNotificationsUseCase() } throws NoConnectionException()
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
    fun `searchNotifications found result`() {
        val query = "date2"
        val expectedList = listOf(notificationsPresentationList[1])
        coEvery { getNotificationsUseCase() } returns notificationsList
        every { notificationPresentationMapper(notificationsList[0]) } returns notificationsPresentationList[0]
        every { notificationPresentationMapper(notificationsList[1]) } returns notificationsPresentationList[1]
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            notificationsPresentationList
        )
        viewModel.searchNotifications(query)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            expectedList
        )
        assertThat(LiveDataTest.getValue(viewModel.notificationsFilterEmpty)).isEqualTo(false)
    }

    @Test
    fun `searchNotifications not found result`() = runBlocking {
        val query = "date3"
        val expectedList = emptyList<NotificationPresentation>()
        coEvery { getNotificationsUseCase() } returns notificationsList
        every { notificationPresentationMapper(notificationsList[0]) } returns notificationsPresentationList[0]
        every { notificationPresentationMapper(notificationsList[1]) } returns notificationsPresentationList[1]
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            notificationsPresentationList
        )
        viewModel.searchNotifications(query)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            expectedList
        )
        assertThat(LiveDataTest.getValue(viewModel.notificationsFilterEmpty)).isEqualTo(true)
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

        private val notificationsPresentationList = listOf(
            NotificationPresentation(
                id = "id1",
                category = "category1",
                title = "title1",
                publisherName = "publisher_name",
                date = "date1",
                seen = true
            ),
            NotificationPresentation(
                id = "id2",
                category = "category2",
                title = "title2",
                publisherName = "publisher_name",
                date = "date2",
                seen = false
            )
        )
    }
}