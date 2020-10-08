package gr.cpaleop.announcements.presentation.categoryfilterdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.announcements.databinding.DialogFragmentCategoryFilterBinding
import gr.cpaleop.teithe_apps.presentation.base.BaseBottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoryFilterDialogFragment : BaseBottomSheetDialog<DialogFragmentCategoryFilterBinding>() {

    private val viewModel: AnnouncementCategoryFilterViewModel by viewModel()
    private val navController: NavController by lazy { findNavController() }
    private var categoryFilterAdapter: CategoryFilterAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentCategoryFilterBinding {
        return DialogFragmentCategoryFilterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentCategories()
    }

    private fun setupViews() {
        categoryFilterAdapter = CategoryFilterAdapter(::navigateToCategoryFilter)
        binding.categoryFilterRecyclerView.adapter = categoryFilterAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::updateLoading))
            categories.observe(viewLifecycleOwner, Observer(::updateCategories))
        }
    }

    private fun updateCategories(categories: List<CategoryFilter>) {
        categoryFilterAdapter?.submitList(categories)
    }

    private fun updateLoading(shouldLoad: Boolean) {
        binding.categoryFilterProgressBar.isVisible = shouldLoad
    }

    private fun navigateToCategoryFilter(categoryId: String) {
        val directions =
            CategoryFilterDialogFragmentDirections.categoryFilterDialogToCategoryFilterActivity(
                categoryId
            )
        navController.navigate(directions)
    }
}