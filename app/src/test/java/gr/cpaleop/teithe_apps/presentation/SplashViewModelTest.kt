package gr.cpaleop.teithe_apps.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.teithe_apps.domain.usecases.AuthenticatedUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    private val testMainDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var authenticatedUseCase: AuthenticatedUseCase

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setupViewModel() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = SplashViewModel(testMainDispatcher, authenticatedUseCase)
    }

    @Test
    fun `checkUserAuthentication user is authenticated`() {
        val expectedValue = true
        coEvery { authenticatedUseCase() } returns expectedValue
        viewModel.checkUserAuthentication()
        assertThat(LiveDataTest.getValue(viewModel.isUserLoggedIn)).isEqualTo(expectedValue)
    }

    @Test
    fun `checkUserAuthentication user is not authenticated`() {
        val expectedValue = false
        coEvery { authenticatedUseCase() } returns expectedValue
        viewModel.checkUserAuthentication()
        assertThat(LiveDataTest.getValue(viewModel.isUserLoggedIn)).isEqualTo(expectedValue)
    }

    @Test
    fun `checkUserAuthentication catches exception and has message when failure`() {
        val expectedMessage = Message(appR.string.error_generic)
        coEvery { authenticatedUseCase() } throws Throwable()
        viewModel.checkUserAuthentication()
        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(expectedMessage)
    }
}