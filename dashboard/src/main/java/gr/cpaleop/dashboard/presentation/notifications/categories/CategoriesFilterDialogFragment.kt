package gr.cpaleop.dashboard.presentation.notifications.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import gr.cpaleop.common.extensions.setEndListener
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.databinding.DialogFragmentCategoriesFilterBinding
import gr.cpaleop.teithe_apps.presentation.base.BaseBottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFilterDialogFragment :
    BaseBottomSheetDialog<DialogFragmentCategoriesFilterBinding>() {

    private val viewModel: CategoriesFilterViewModel by viewModel()
    private var categoryFilterAdapter: CategoryFilterAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentCategoriesFilterBinding {
        return DialogFragmentCategoriesFilterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentCategories()
    }

    private fun setupViews() {
        categoryFilterAdapter = CategoryFilterAdapter(viewModel::updateSelectedCategories)
        binding.categoryFilterRecyclerView.run {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter = categoryFilterAdapter
        }

        binding.categoryFilterResetText.setOnClickListener {
            viewModel.clearSelections()
        }

        binding.categoryFilterSubmitTextView.setOnClickListener {
            viewModel.updateRegisteredCategories()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            categories.observe(viewLifecycleOwner, Observer(::updateCategories))
            resetButtonControl.observe(viewLifecycleOwner, Observer(::updateResetButton))
        }
    }

    private fun updateCategories(categories: List<Category>) {
        categoryFilterAdapter?.submitList(categories)
    }

    private fun updateResetButton(shouldShow: Boolean) {
        if (shouldShow == binding.categoryFilterResetText.isEnabled) return

        val alpha = if (shouldShow) 1f else 0f
        binding.categoryFilterResetText.animate()
            .alpha(alpha)
            .setDuration(300)
            .setEndListener { binding.categoryFilterResetText.isEnabled = shouldShow }
            .start()
    }

    private fun updateLoader(shouldLoad: Boolean) {
        binding.categoryFilterProgressBar.visibility =
            if (shouldLoad) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val CATEGORIES_FILTER_DIALOG_NAME = "CATEGORIES_FILTER_DIALOG_NAME"
    }
}