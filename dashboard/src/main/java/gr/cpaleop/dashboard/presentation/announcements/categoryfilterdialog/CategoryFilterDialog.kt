package gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentCategoryFilterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFilterDialog : BottomSheetDialogFragment() {

    private val viewModel: CategoryFilterViewModel by viewModel()
    private var _binding: DialogFragmentCategoryFilterBinding? = null
    private val binding: DialogFragmentCategoryFilterBinding get() = _binding!!
    private var categoryFilterAdapter: CategoryFilterAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_category_filter, container, false)
        _binding =
            DialogFragmentCategoryFilterBinding.bind(view).setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentCategories()
    }

    private fun setupViews() {
        categoryFilterAdapter = CategoryFilterAdapter()
        binding.categoryFilterRecyclerView.adapter = categoryFilterAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            categories.observe(viewLifecycleOwner, Observer(::updateCategories))
        }
    }

    private fun updateCategories(categories: List<Category>) {
        categoryFilterAdapter?.submitList(categories)
    }

    companion object {

        const val CATEGORY_FILTER_DIALOG_NAME = "CATEGORY_FILTER_DIALOG_NAME"
    }
}