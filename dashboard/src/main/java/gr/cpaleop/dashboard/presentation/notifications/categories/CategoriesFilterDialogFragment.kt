package gr.cpaleop.dashboard.presentation.notifications.categories

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentCategoriesFilterBinding
import gr.cpaleop.dashboard.domain.entities.Category
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFilterDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: CategoriesFilterViewModel by viewModel()
    private var _binding: DialogFragmentCategoriesFilterBinding? = null
    private val binding: DialogFragmentCategoriesFilterBinding get() = _binding!!
    private var categoryFilterAdapter: CategoryFilterAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_categories_filter, container, false)
        _binding =
            DialogFragmentCategoriesFilterBinding.bind(view).setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentCategories()
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
            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
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

    private fun setupViews() {
        categoryFilterAdapter = CategoryFilterAdapter(viewModel::updateCategories)
        binding.categoryFilterRecyclerView.run {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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
            loading.observe(viewLifecycleOwner, Observer(::toggleLoad))
            categories.observe(viewLifecycleOwner, Observer(::showCategories))
            resetButtonControl.observe(viewLifecycleOwner, Observer(::toggleResetButton))
        }
    }

    private fun showCategories(categories: List<Category>) {
        categoryFilterAdapter?.submitList(categories)
    }

    private fun toggleResetButton(shouldShow: Boolean) {
        if (shouldShow == binding.categoryFilterResetText.isEnabled) return

        val alpha = if (shouldShow) 1f else 0f
        binding.categoryFilterResetText.animate()
            .alpha(alpha)
            .setDuration(300)
            .setEndListener { binding.categoryFilterResetText.isEnabled = shouldShow }
            .start()
    }

    private fun toggleLoad(shouldLoad: Boolean) {
        binding.categoryFilterProgressBar.visibility =
            if (shouldLoad) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val CATEGORIES_FILTER_DIALOG_NAME = "CATEGORIES_FILTER_DIALOG_NAME"
    }
}