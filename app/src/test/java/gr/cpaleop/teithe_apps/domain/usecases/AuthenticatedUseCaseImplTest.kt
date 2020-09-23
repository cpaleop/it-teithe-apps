package gr.cpaleop.teithe_apps.domain.usecases

import com.google.common.truth.Truth.assertThat
import gr.cpaleop.teithe_apps.domain.repositories.PreferencesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
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
        coEvery { preferencesRepository.getAccessToken() } returns "access_token"
        coEvery { preferencesRepository.getRefreshToken() } returns "refresh_token"
        val actualValue = authenticatedUseCase()
        assertThat(actualValue).isEqualTo(expectedValue)
    }

    @Test
    fun `invoke returns false when there is null access token and there is refresh token`() =
        runBlocking {
            val expectedValue = false
            coEvery { preferencesRepository.getAccessToken() } returns null
            coEvery { preferencesRepository.getRefreshToken() } returns "refresh_token"
            val actualValue = authenticatedUseCase()
            assertThat(actualValue).isEqualTo(expectedValue)
        }

    @Test
    fun `invoke returns false when there is empty access token and there is refresh token`() =
        runBlocking {
            val expectedValue = false
            coEvery { preferencesRepository.getAccessToken() } returns ""
            coEvery { preferencesRepository.getRefreshToken() } returns "refresh_token"
            val actualValue = authenticatedUseCase()
            assertThat(actualValue).isEqualTo(expectedValue)
        }

    @Test
    fun `invoke returns false when there is access token and there is null refresh token`() =
        runBlocking {
            val expectedValue = false
            coEvery { preferencesRepository.getAccessToken() } returns "access_token"
            coEvery { preferencesRepository.getRefreshToken() } returns null
            val actualValue = authenticatedUseCase()
            assertThat(actualValue).isEqualTo(expectedValue)
        }

    @Test
    fun `invoke returns false when there is access token and there is empty refresh token`() =
        runBlocking {
            val expectedValue = false
            coEvery { preferencesRepository.getAccessToken() } returns "access_token"
            coEvery { preferencesRepository.getRefreshToken() } returns ""
            val actualValue = authenticatedUseCase()
            assertThat(actualValue).isEqualTo(expectedValue)
        }
}