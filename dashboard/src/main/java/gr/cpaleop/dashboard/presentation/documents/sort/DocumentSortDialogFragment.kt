package gr.cpaleop.dashboard.presentation.documents.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.core.presentation.BaseBottomSheetDialog
import gr.cpaleop.dashboard.databinding.DialogFragmentSortDocumentsBinding
import gr.cpaleop.dashboard.domain.entities.DocumentSort
import gr.cpaleop.dashboard.domain.entities.DocumentSortType
import gr.cpaleop.dashboard.presentation.documents.DocumentsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DocumentSortDialogFragment : BaseBottomSheetDialog<DialogFragmentSortDocumentsBinding>() {

    private val viewModel: DocumentsViewModel by sharedViewModel()
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
            refresh.observe(viewLifecycleOwner, Observer { dismiss() })
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