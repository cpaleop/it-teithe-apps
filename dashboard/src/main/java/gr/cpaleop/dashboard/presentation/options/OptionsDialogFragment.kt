package gr.cpaleop.dashboard.presentation.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentOptionsBinding
import gr.cpaleop.dashboard.presentation.options.sort.SortOption
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: OptionsViewModel by viewModel()
    private var _binding: DialogFragmentOptionsBinding? = null
    private val binding: DialogFragmentOptionsBinding get() = _binding!!

    private var optionsAdapter: OptionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_options, container, false)
        _binding = DialogFragmentOptionsBinding.bind(view).setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentSortOptions()
    }

    private fun setupViews() {
        optionsAdapter = OptionsAdapter()
        binding.fileOptionsRecyclerView.adapter = optionsAdapter
    }

    private fun observeViewModel() {
        viewModel.options.observe(viewLifecycleOwner, Observer(::showSortOptions))
    }

    private fun showSortOptions(optionList: List<SortOption>) {
        optionsAdapter?.submitList(optionList)
    }
}