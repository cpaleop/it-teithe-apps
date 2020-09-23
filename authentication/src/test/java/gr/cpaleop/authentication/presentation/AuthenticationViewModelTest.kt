package gr.cpaleop.authentication.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.authentication.domain.usecases.BuildUriUseCase
import gr.cpaleop.authentication.domain.usecases.RetrieveTokenUseCase
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.teithe_apps.di.dispatchers.MainDispatcher
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthenticationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var retrieveTokenUseCase: RetrieveTokenUseCase

    @MockK
    private lateinit var buildUriUseCase: BuildUriUseCase

    private lateinit var viewModel: AuthenticationViewModel

    @Before()
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = AuthenticationViewModel(
            testCoroutineDispatcher,
            retrieveTokenUseCase,
            buildUriUseCase
        )
    }

    @Test
    fun `retrieveToken success`() {
        val code = "code"
        coEvery { retrieveTokenUseCase(code) } returns Unit
        viewModel.retrieveToken(code)
        assertThat(LiveDataTest.getValue(viewModel.tokenRetrieved)).isEqualTo(Unit)
    }

    @Test
    fun `retrieveToken catches exception`() {
        val code = "code"
        coEvery { retrieveTokenUseCase(code) } throws Throwable("")
        viewModel.retrieveToken(code)
    }

    @Test
    fun `presentUri success`() {
        val expectedUri = "some_uri"
        every { buildUriUseCase() } returns expectedUri
        viewModel.presentUri()
        assertThat(LiveDataTest.getValue(viewModel.uri)).isEqualTo(expectedUri)
    }

    @Test
    fun `presentUri catches exception`() {
        every { buildUriUseCase() } throws Throwable("")
        viewModel.presentUri()
    }
}