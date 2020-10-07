package gr.cpaleop.categoryfilter.presentation

import android.os.Bundle
import gr.cpaleop.categoryfilter.databinding.ActivityCategoryFilterBinding
import gr.cpaleop.categoryfilter.di.categoryFilterModule
import gr.cpaleop.teithe_apps.presentation.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
@FlowPreview
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

    override fun finish() {
        super.finish()
        overridePendingTransition(
            appR.anim.pop_enter_fade_in,
            appR.anim.fade_out,
        )
    }

    private fun handleIntent() {
        viewModel.categoryId = intent.getStringExtra(CATEGORY_ID) ?: ""
    }

    companion object {

        private const val CATEGORY_ID = "categoryId"
    }
}