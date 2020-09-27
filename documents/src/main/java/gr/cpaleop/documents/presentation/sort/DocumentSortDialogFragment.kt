package gr.cpaleop.documents.presentation.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.core.presentation.BaseBottomSheetDialog
import gr.cpaleop.documents.databinding.DialogFragmentSortDocumentsBinding
import gr.cpaleop.documents.domain.entities.DocumentSort
import gr.cpaleop.documents.domain.entities.DocumentSortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class DocumentSortDialogFragment : BaseBottomSheetDialog<DialogFragmentSortDocumentsBinding>() {

    private val viewModel: DocumentSortOptionsViewModel by viewModel()
    private var documentSortOptionsAdapter: DocumentSortOptionsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentSortDocumentsBinding {
        return DialogFragmentSortDocumentsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentDocumentSortOptions()
    }

    private fun setupViews() {
        documentSortOptionsAdapter = DocumentSortOptionsAdapter(::updateDocumentSort)
        binding.filesSortOptionsRecyclerView.adapter = documentSortOptionsAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            dismissDialog.observe(viewLifecycleOwner, { dismiss() })
            documentSortOptions.observe(viewLifecycleOwner, Observer(::updateFileSortOptions))
        }
    }

    private fun updateFileSortOptions(documentSortOptions: List<DocumentSortOption>) {
        documentSortOptionsAdapter?.submitList(documentSortOptions)
    }

    private fun updateDocumentSort(
        @DocumentSortType type: Int,
        descending: Boolean,
        selected: Boolean
    ) {
        val descend = if (!selected) descending else !descending
        val documentSort = DocumentSort(type = type, descending = descend, selected = true)
        viewModel.updateSort(documentSort)
    }
}