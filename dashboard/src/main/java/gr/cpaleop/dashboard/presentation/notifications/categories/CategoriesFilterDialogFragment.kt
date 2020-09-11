package gr.cpaleop.dashboard.presentation.notifications.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentCategoriesFilterBinding
import gr.cpaleop.dashboard.domain.entities.Category
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CategoriesFilterDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: CategoriesFilterViewModel by viewModel()
    private var _binding: DialogFragmentCategoriesFilterBinding? = null
    private val binding: DialogFragmentCategoriesFilterBinding get() = _binding!!

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

    private fun setupViews() {

    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::toggleLoad))
            categories.observe(viewLifecycleOwner, Observer(::showCategories))
        }
    }

    private fun showCategories(categories: List<Category>) {
        categories.forEach {
            Timber.e(it.name, it.isRegistered)
        }
    }

    private fun toggleLoad(shouldLoad: Boolean) {

    }

    companion object {
        const val CATEGORIES_FILTER_DIALOG_NAME = "CATEGORIES_FILTER_DIALOG_NAME"
    }
}