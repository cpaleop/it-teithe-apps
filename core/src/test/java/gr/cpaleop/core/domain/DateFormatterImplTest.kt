package gr.cpaleop.core.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DateFormatterImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dateFormatter: DateFormatterImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        dateFormatter = DateFormatterImpl()
    }

    @Test
    fun `invoke transforms to human readable format when given epoch`() {
        val givenEpoch = 1600898798487L
        val expected = "19-06-52700, 09:41"
        val actual = dateFormatter(givenEpoch)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke transforms to given human readable format when given epoch`() {
        val givenEpoch = 1600898798487L
        val givenHumanReadableFormat = "dd-MM-yyyy, HH:mm"
        val expected = "19-06-52700, 09:41"
        val actual = dateFormatter(givenEpoch, givenHumanReadableFormat)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke transforms to given human readable format when given date is ISO 8601 format`() {
        val givenISO8601Date = "2020-09-18T08:57:03.972Z"
        val givenTargetHumanReadableFormat = "dd-MM-yyyy, HH:mm"
        val expected = "18-09-2020, 08:57"
        val actual = dateFormatter(givenISO8601Date, givenTargetHumanReadableFormat)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke transforms to epoch when given date is ISO 8601 format`() {
        val givenISO8601Date = "2020-09-18T08:57:03.972Z"
        val expected = 1600408623972L
        val actual = dateFormatter(givenISO8601Date)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `fileFormat transforms given epoch when given human readable format`() {
        val givenEpoch = 1600408623972L
        val givenTargetHumanReadableFormat = "dd-MM-yyyy, HH:mm"
        val expected = "18-09-2020, 08:57"
        val actual = dateFormatter.fileFormat(givenEpoch, givenTargetHumanReadableFormat)
        assertThat(actual).isEqualTo(expected)
    }
}