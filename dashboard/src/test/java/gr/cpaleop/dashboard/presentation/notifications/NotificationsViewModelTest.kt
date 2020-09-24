package gr.cpaleop.dashboard.presentation.notifications

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.dashboard.domain.entities.Notification
import gr.cpaleop.dashboard.domain.entities.NotificationRelatedAnnouncement
import gr.cpaleop.dashboard.domain.usecases.GetNotificationsUseCase
import gr.cpaleop.dashboard.domain.usecases.ReadAllNotificationsUseCase
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotificationsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getNotificationsUseCase: GetNotificationsUseCase

    @MockK
    private lateinit var readAllNotificationsUseCase: ReadAllNotificationsUseCase

    private lateinit var viewModel: NotificationsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = NotificationsViewModel(
            testCoroutineDispatcher,
            getNotificationsUseCase,
            readAllNotificationsUseCase
        )
    }

    @Test
    fun `presentNotifications notifications not empty`() {
        coEvery { getNotificationsUseCase() } returns notificationsList
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.notificationsEmpty)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            notificationsList
        )
        assertThat(LiveDataTest.getValue(viewModel.notificationsCounter)).isEqualTo(1)
    }

    @Test
    fun `presentNotifications notifications empty`() {
        coEvery { getNotificationsUseCase() } returns notificationsEmptyList
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.loading)).isEqualTo(false)
        assertThat(LiveDataTest.getValue(viewModel.notificationsEmpty)).isEqualTo(true)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            notificationsEmptyList
        )
        assertThat(LiveDataTest.getValue(viewModel.notificationsCounter)).isEqualTo(0)
    }

    @Test
    fun `readAllNotifications success`() {
        coEvery { readAllNotificationsUseCase() } returns Unit
        viewModel.readAllNotifications()
    }

    @Test
    fun `readAllNotifications when fails catches exception`() {
        coEvery { readAllNotificationsUseCase() } throws Throwable()
        viewModel.readAllNotifications()
    }

    @Test
    fun `searchNotifications found result`() {
        val query = "date2"
        val expectedList = listOf(notificationsList[1])
        coEvery { getNotificationsUseCase() } returns notificationsList
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(notificationsList)
        viewModel.searchNotifications(query)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            expectedList
        )
        assertThat(LiveDataTest.getValue(viewModel.notificationsFilterEmpty)).isEqualTo(false)
    }

    @Test
    fun `searchNotifications not found result`() = runBlocking {
        val query = "date3"
        val expectedList = emptyList<Notification>()
        coEvery { getNotificationsUseCase() } returns notificationsList
        viewModel.presentNotifications()
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(notificationsList)
        viewModel.searchNotifications(query)
        assertThat(LiveDataTest.getValue(viewModel.notifications)).isEqualTo(
            expectedList
        )
        assertThat(LiveDataTest.getValue(viewModel.notificationsFilterEmpty)).isEqualTo(true)
    }

    companion object {

        private val notificationsEmptyList = emptyList<Notification>()
        private val notificationsList = listOf(
            Notification(
                id = "id1",
                announcement = NotificationRelatedAnnouncement(
                    id = "id1",
                    category = "category1",
                    date = "date1",
                    title = "title1"
                ),
                seen = true
            ),
            Notification(
                id = "id2",
                announcement = NotificationRelatedAnnouncement(
                    id = "id2",
                    category = "category2",
                    date = "date2",
                    title = "title2"
                ),
                seen = false
            )
        )
    }
}