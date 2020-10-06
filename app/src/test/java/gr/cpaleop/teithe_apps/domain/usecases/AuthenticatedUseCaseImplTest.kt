package gr.cpaleop.teithe_apps.domain.usecases

import com.google.common.truth.Truth.assertThat
import gr.cpaleop.core.domain.entities.Token
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthenticatedUseCaseImplTest {

    @MockK
    private lateinit var preferencesRepository: PreferencesRepository

    private lateinit var authenticatedUseCase: AuthenticatedUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        authenticatedUseCase = AuthenticatedUseCaseImpl(preferencesRepository)
    }

    @Test
    fun `invoke returns true when there is access and refresh token`() = runBlocking {
        val expectedValue = true
        val givenTokenFlow = flow {
            emit(Token("access_token", "refresh_token"))
        }
        coEvery { preferencesRepository.getTokenFlow() } returns givenTokenFlow
        val actualValue = authenticatedUseCase()
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    @Test
    fun `invoke returns false when there is blank access token and there is refresh token`() =
        runBlocking {
            val expectedValue = false
            val givenTokenFlow = flow {
                emit(Token("", "refresh_token"))
            }
            coEvery { preferencesRepository.getTokenFlow() } returns givenTokenFlow
            val actualValue = authenticatedUseCase()
            assertThat(actualValue).isEqualTo(expectedValue)
        }

    @Test
    fun `invoke returns false when there is access token and there is null refresh token`() =
        runBlocking {
            val expectedValue = false
            val givenTokenFlow = flow {
                emit(Token("access_token", ""))
            }
            coEvery { preferencesRepository.getTokenFlow() } returns givenTokenFlow
            val actualValue = authenticatedUseCase()
            assertThat(actualValue).isEqualTo(expectedValue)
        }
}