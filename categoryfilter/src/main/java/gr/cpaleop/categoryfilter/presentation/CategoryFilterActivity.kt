package gr.cpaleop.categoryfilter.presentation

import android.os.Bundle
import gr.cpaleop.categoryfilter.databinding.ActivityCategoryFilterBinding
import gr.cpaleop.categoryfilter.di.categoryFilterModule
import gr.cpaleop.core.presentation.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CategoryFilterActivity : BaseActivity<ActivityCategoryFilterBinding>() {

    private val viewModel: CategoryFilterViewModel by viewModel()

    override fun inflateViewBinding(): ActivityCategoryFilterBinding {
        return ActivityCategoryFilterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(categoryFilterModule)
        super.onCreate(savedInstanceState)
        handleIntent()
    }

    override fun onDestroy() {
        unloadKoinModules(categoryFilterModule)
        super.onDestroy()
    }

    private fun handleIntent() {
        viewModel.categoryId = intent.getStringExtra(CATEGORY_ID) ?: ""
    }

    companion object {

        private const val CATEGORY_ID = "categoryId"
    }
}