package gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentCategoryFilterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.FrameLayout as FrameLayout1

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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            setupRatio(it as BottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout1>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet ?: return)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getWindowHeight() * 90 / 100
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentCategories()
    }

    private fun setupViews() {
        categoryFilterAdapter = CategoryFilterAdapter(viewModel::selectCategory)
        binding.categoryFilterRecyclerView.adapter = categoryFilterAdapter

        binding.categoryFilterSubmitButton.setOnClickListener {
            viewModel.computeSelectedCategories()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::updateLoading))
            categories.observe(viewLifecycleOwner, Observer(::updateCategories))
            submitButtonControl.observe(viewLifecycleOwner, Observer(::updateSubmitButton))
            selectedCategories.observe(viewLifecycleOwner, Observer(::navigateToCategoryFilter))
        }
    }

    private fun updateCategories(categories: List<CategoryFilter>) {
        categoryFilterAdapter?.submitList(categories)
    }

    private fun updateLoading(shouldLoad: Boolean) {
        binding.categoryFilterProgressBar.isVisible = shouldLoad
    }

    private fun updateSubmitButton(shouldShow: Boolean) {
        binding.categoryFilterSubmitButton.isVisible = shouldShow
    }

    private fun navigateToCategoryFilter(categoryIdList: List<String>) {
        TODO("Navigate to CategoryFilterActivity")
    }

    companion object {

        const val CATEGORY_FILTER_DIALOG_NAME = "CATEGORY_FILTER_DIALOG_NAME"
    }
}