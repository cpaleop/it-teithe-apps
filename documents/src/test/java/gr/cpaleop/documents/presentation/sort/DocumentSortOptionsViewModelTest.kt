package gr.cpaleop.documents.presentation.sort

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import gr.cpaleop.common_test.LiveDataTest
import gr.cpaleop.core.dispatchers.DefaultDispatcher
import gr.cpaleop.core.dispatchers.MainDispatcher
import gr.cpaleop.documents.R
import gr.cpaleop.documents.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.entities.DocumentSortType
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
class DocumentSortOptionsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MainDispatcher
    private val mainDispatcher = TestCoroutineDispatcher()

    @DefaultDispatcher
    private val defaultDispatcher = TestCoroutineDispatcher()

    @MockK
    private lateinit var getDocumentSortOptionsUseCase: gr.cpaleop.documents.domain.usecases.GetDocumentSortOptionsUseCase

    @MockK
    private lateinit var documentSortOptionMapper: DocumentSortOptionMapper

    @MockK
    private lateinit var observeDocumentSortUseCase: gr.cpaleop.documents.domain.usecases.ObserveDocumentSortUseCase

    private lateinit var viewModel: DocumentSortOptionsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = DocumentSortOptionsViewModel(
            mainDispatcher,
            defaultDispatcher,
            getDocumentSortOptionsUseCase,
            documentSortOptionMapper,
            observeDocumentSortUseCase
        )
    }

    @Test
    fun `presentDocumentSortOptions has correct values`() {
        val expected = documentSortOptionsList
        coEvery { getDocumentSortOptionsUseCase() } returns documentSortList
        every { documentSortOptionMapper(documentSortList[0]) } returns documentSortOptionsList[0]
        every { documentSortOptionMapper(documentSortList[1]) } returns documentSortOptionsList[1]
        viewModel.presentDocumentSortOptions()
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptions)).isEqualTo(expected)
    }

    @Test
    fun `presentDocumentSortOptions catches exception`() {
        coEvery { getDocumentSortOptionsUseCase() } throws Throwable()
        viewModel.presentDocumentSortOptions()
    }

    @Test
    fun `updateSort is successful`() {
        val documentSort = DocumentSort(
            type = DocumentSortType.ALPHABETICAL,
            selected = true,
            descending = true
        )
        coEvery { observeDocumentSortUseCase.update(documentSort) } returns Unit
        viewModel.updateSort(documentSort)
        assertThat(LiveDataTest.getValue(viewModel.dismissDialog)).isEqualTo(Unit)
    }

    @Test
    fun `updateSort catches exception`() {
        val documentSort = DocumentSort(
            type = DocumentSortType.ALPHABETICAL,
            selected = true,
            descending = true
        )
        coEvery { observeDocumentSortUseCase.update(documentSort) } throws Throwable()
        viewModel.updateSort(documentSort)
    }

    //TODO: Move to DocumentSortOptionsViewModelTest
    /*@Test
    fun `presentDocumentSortOptions success`() {
        val expected = documentSortOptionsList
        val expectedSortOptionSelected = documentSortOptionsList[0]
        coEvery { getDocumentSortOptionsUseCase() } returns documentSortList
        every { documentSortOptionMapper(documentSortList[0]) } returns documentSortOptionsList[0]
        every { documentSortOptionMapper(documentSortList[1]) } returns documentSortOptionsList[1]
        viewModel.presentDocumentSortOptions()
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptions)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptionSelected)).isEqualTo(
            expectedSortOptionSelected
        )
    }

    @Test
    fun `presentDocumentSortOptions when fails catches exception`() {
        coEvery { getDocumentSortOptionsUseCase() } throws Throwable()
        viewModel.presentDocumentSortOptions()
    }

    @Test
    fun `updateSort success`() {
        val givenType = DocumentSortType.DATE
        val givenDescending = true
        val givenSelected = true
        val givenDocumentSort = DocumentSort(givenType, givenSelected, givenDescending)
        val expected = selectedDocumentSortOption
        coEvery { updateDocumentSortUseCase(givenDocumentSort) } returns selectedDocumentSort
        every { documentSortOptionMapper(selectedDocumentSort) } returns selectedDocumentSortOption
        viewModel.updateSort(givenDocumentSort)
        assertThat(LiveDataTest.getValue(viewModel.documentSortOptionSelected)).isEqualTo(expected)
        assertThat(LiveDataTest.getValue(viewModel.refresh)).isEqualTo(Unit)
    }

    @Test
    fun `updateSort when fails catches exception`() {
        val givenType = DocumentSortType.DATE
        val givenDescending = true
        val givenSelected = true
        val givenDocumentSort = DocumentSort(givenType, givenSelected, givenDescending)
        coEvery { updateDocumentSortUseCase(givenDocumentSort) } throws Throwable()
        viewModel.updateSort(givenDocumentSort)
    }*/

    companion object {

        private val documentSortOptionsList = listOf(
            DocumentSortOption(
                type = DocumentSortType.DATE,
                imageResource = R.drawable.ic_arrow_down,
                labelResource = R.string.documents_sort_date,
                selected = true,
                descending = true
            ),
            DocumentSortOption(
                type = DocumentSortType.ALPHABETICAL,
                imageResource = R.drawable.ic_arrow_up,
                labelResource = R.string.documents_sort_alphabetical,
                selected = false,
                descending = false
            )
        )

        private val documentSortList = listOf(
            DocumentSort(
                type = DocumentSortType.DATE,
                selected = true,
                descending = true
            ),
            DocumentSort(
                type = DocumentSortType.ALPHABETICAL,
                selected = false,
                descending = false
            )
        )
    }
}