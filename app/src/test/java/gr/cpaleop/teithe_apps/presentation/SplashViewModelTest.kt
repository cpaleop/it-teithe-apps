package gr.cpaleop.teithe_apps.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.teithe_apps.di.dispatchers.DefaultDispatcher
import gr.cpaleop.teithe_apps.domain.usecases.AuthenticatedUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @DefaultDispatcher
    private val testDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var authenticatedUseCase: AuthenticatedUseCase

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setupViewModel() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = SplashViewModel(testDispatcher, authenticatedUseCase)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
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
    fun `checkUserAuthentication catches exception`() {
        coEvery { authenticatedUseCase() } throws Throwable("")
        viewModel.checkUserAuthentication()
    }
}