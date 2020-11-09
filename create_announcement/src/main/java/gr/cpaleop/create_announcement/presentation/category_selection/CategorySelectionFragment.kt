package gr.cpaleop.create_announcement.presentation.category_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.databinding.DialogFragmentCategorySelectionBinding
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseBottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CategorySelectionFragment : BaseBottomSheetDialog<DialogFragmentCategorySelectionBinding>() {

    private val viewModel: CreateAnnouncementViewModel by sharedViewModel()
    private var categorySelectionAdapter: CategorySelectionAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentCategorySelectionBinding {
        return DialogFragmentCategorySelectionBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentCategories()
    }

    private fun setupViews() {
        categorySelectionAdapter = CategorySelectionAdapter(viewModel::selectCategory)
        binding.categorySelectionRecyclerView.adapter = categorySelectionAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            categories.observe(viewLifecycleOwner, Observer(::updateCategories))
            categorySelected.observe(viewLifecycleOwner, { dismissAllowingStateLoss() })
        }
    }

    private fun updateCategories(categoryList: List<Category>) {
        categorySelectionAdapter?.submitList(categoryList)
    }
}