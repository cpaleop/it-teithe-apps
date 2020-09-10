package gr.cpaleop.dashboard.presentation.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.databinding.FragmentNotificationsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    private val viewModel: NotificationsViewModel by viewModel()
    private val navController: NavController by lazy { findNavController() }
    private var notificationAdapter: NotificationAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationsBinding {
        return FragmentNotificationsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        binding.notificationsShimmerLayout.startShimmer()
        viewModel.presentNotifications()
    }

    private fun setupViews() {
        notificationAdapter = NotificationAdapter(::navigateToAnnouncement)
        binding.notificationsRecyclerView.adapter = notificationAdapter

        binding.notificationsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.presentNotifications()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::toggleLoading))
            notifications.observe(viewLifecycleOwner, Observer(::updateNotifications))
            notificationsEmpty.observe(viewLifecycleOwner, Observer(::showNotificationsEmpty))
        }
    }

    private fun updateNotifications(notifications: List<NotificationPresentation>) {
        binding.notificationsShimmerLayout.apply {
            stopShimmer()
            isVisible = false
        }
        notificationAdapter?.submitList(notifications)
    }

    private fun showNotificationsEmpty(notificationsEmpty: Boolean) {
        binding.notificationsEmptyTextView.isVisible = notificationsEmpty
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val directions = NotificationsFragmentDirections.announcementsToAnnouncement(announcementId)
        navController.navigate(directions)
    }

    private fun toggleLoading(shouldLoad: Boolean) {
        binding.notificationsSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}