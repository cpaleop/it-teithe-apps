package gr.cpaleop.dashboard.presentation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import gr.cpaleop.core.presentation.BaseActivity
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.ActivityDashboardBinding
import gr.cpaleop.dashboard.di.dashboardModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    private val navController: NavController by lazy { findNavController(R.id.dashboardFragmentHost) }

    override fun inflateViewBinding(): ActivityDashboardBinding {
        return ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(dashboardModule)
        super.onCreate(savedInstanceState)
        setupViews()
    }

    override fun onDestroy() {
        unloadKoinModules(dashboardModule)
        super.onDestroy()
    }

    private fun setupViews() {
        binding.dashboardBottomNavigationView.setupWithNavController(navController)
    }
}