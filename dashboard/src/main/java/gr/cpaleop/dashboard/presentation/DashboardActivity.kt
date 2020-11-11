package gr.cpaleop.dashboard.presentation

import android.app.ActivityOptions
import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.ActivityDashboardBinding
import gr.cpaleop.dashboard.di.dashboardModule
import gr.cpaleop.dashboard.presentation.notifications.NotificationsViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    private val notificationsViewModel: NotificationsViewModel by viewModel()
    private val navController: NavController by lazy { findNavController(R.id.dashboardFragmentHost) }

    override fun inflateViewBinding(): ActivityDashboardBinding {
        return ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(dashboardModule)
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupViews()
    }

    override fun onDestroy() {
        unloadKoinModules(dashboardModule)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        notificationsViewModel.presentNotifications()
    }

    private fun setupViews() {
        binding.dashboardBottomNavigationView.run {
            setupWithNavController(navController)
            setOnNavigationItemReselectedListener { }
        }

        binding.createAnnouncementFab.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                it,
                "fab_transition"
            )
            val optionss = NavOptions.Builder()
                .setEnterAnim(appR.anim.slide_in_bottom)
                .setExitAnim(appR.anim.wait_anim)
                .build()

            navController.navigate(R.id.createAnnouncementActivity, options.toBundle(), optionss)
        }
    }

    private fun observeViewModel() {
        notificationsViewModel.notificationsCounter.observe(
            this@DashboardActivity,
            Observer(::updateNotificationsCounterBadge)
        )
    }

    private fun updateNotificationsCounterBadge(notificationsCounter: Int) {
        binding.dashboardBottomNavigationView.post {
            binding.dashboardBottomNavigationView.getOrCreateBadge(R.id.notificationsFragment).run {
                isVisible = notificationsCounter != 0
                badgeTextColor = Color.WHITE
                number = notificationsCounter
            }
        }
    }
}