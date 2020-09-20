package gr.cpaleop.dashboard.presentation.announcements.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.core.presentation.BaseBottomSheetDialog
import gr.cpaleop.dashboard.databinding.DialogFragmentSortOptionsBinding
import gr.cpaleop.dashboard.presentation.announcements.options.sort.SortOption
import org.koin.androidx.viewmodel.ext.android.viewModel

class SortOptionsDialogFragment : BaseBottomSheetDialog<DialogFragmentSortOptionsBinding>() {

    private val viewModel: SortOptionsViewModel by viewModel()
    private var sortOptionsAdapter: SortOptionsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentSortOptionsBinding {
        return DialogFragmentSortOptionsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentSortOptions()
    }

    private fun setupViews() {
        sortOptionsAdapter = SortOptionsAdapter()
        binding.sortOptionsRecyclerView.adapter = sortOptionsAdapter
    }

    private fun observeViewModel() {
        viewModel.options.observe(viewLifecycleOwner, Observer(::showSortOptions))
    }

    private fun showSortOptions(optionList: List<SortOption>) {
        sortOptionsAdapter?.submitList(optionList)
    }

    companion object {

        const val OPTIONS_DIALOG_NAME = "OPTIONS_DIALOG_NAME"
    }
}