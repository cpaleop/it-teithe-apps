package gr.cpaleop.authentication.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BuildUriUseCaseImplTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var buildUriUseCase: BuildUriUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        buildUriUseCase = BuildUriUseCaseImpl()
    }

    @Test
    fun `invoke returns correct value`() {
        val expectedValue = "https://login.it.teithe.gr/" +
                "authorization/" +
                "?client_id=5bd3ab44ae4ff2273941c011" +
                "&response_type=code" +
                "&scope=announcements," +
                "edit_mail," +
                "edit_password," +
                "edit_profile," +
                "notifications," +
                "profile," +
                "refresh_token" +
                "&redirect_uri=appsservice://callback"
        val actual = buildUriUseCase()
        assertThat(actual).isEqualTo(expectedValue)
    }
}