package gr.cpaleop.dashboard.presentation.files.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.core.presentation.BaseBottomSheetDialog
import gr.cpaleop.dashboard.databinding.DialogFragmentSortFilesBinding
import gr.cpaleop.dashboard.presentation.files.DocumentsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FileSortDialogFragment : BaseBottomSheetDialog<DialogFragmentSortFilesBinding>() {

    private val viewModel: DocumentsViewModel by sharedViewModel()
    private var fileSortOptionsAdapter: FileSortOptionsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentSortFilesBinding {
        return DialogFragmentSortFilesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentDocumentSortOptions()
    }

    private fun setupViews() {
        fileSortOptionsAdapter = FileSortOptionsAdapter(viewModel::updateSort)
        binding.filesSortOptionsRecyclerView.adapter = fileSortOptionsAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            refresh.observe(viewLifecycleOwner, Observer { dismiss() })
            documentSortOptions.observe(viewLifecycleOwner, Observer(::updateFileSortOptions))
        }
    }

    private fun updateFileSortOptions(documentSortOptions: List<DocumentSortOption>) {
        fileSortOptionsAdapter?.submitList(documentSortOptions)
    }
}