package gr.cpaleop.dashboard.presentation.announcements.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentSortOptionsBinding
import gr.cpaleop.dashboard.presentation.announcements.options.sort.SortOption
import org.koin.androidx.viewmodel.ext.android.viewModel

class SortOptionsDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: SortOptionsViewModel by viewModel()
    private var _binding: DialogFragmentSortOptionsBinding? = null
    private val binding: DialogFragmentSortOptionsBinding get() = _binding!!

    private var sortOptionsAdapter: SortOptionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_sort_options, container, false)
        _binding = DialogFragmentSortOptionsBinding.bind(view).setLifecycleOwner(viewLifecycleOwner)
        return binding.root
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