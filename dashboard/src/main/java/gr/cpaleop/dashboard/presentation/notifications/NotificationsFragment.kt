package gr.cpaleop.dashboard.presentation.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.common.extensions.animateVisibilty
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentNotificationsBinding
import gr.cpaleop.dashboard.presentation.notifications.categories.CategoriesFilterDialogFragment
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
class NotificationsFragment :
    BaseApiFragment<FragmentNotificationsBinding, NotificationsViewModel>(NotificationsViewModel::class) {

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
        binding.root.hideKeyboard()
        setupViews()
        observeViewModel()
        viewModel.readAllNotifications()
    }

    private fun setupViews() {
        notificationAdapter = NotificationAdapter(::navigateToAnnouncement)
        binding.notificationsRecyclerView.adapter = notificationAdapter

        binding.notificationsSwipeRefreshLayout.setOnRefreshListener {
            binding.notificationsSearchTextView.setText(requireContext().getString(appR.string.empty))
            viewModel.presentNotifications()
        }

        binding.notificationsAddFilterFab.setOnClickListener {
            openCategoriesFilterDialog()
        }

        binding.notificationsSearchTextView.run {
            doOnTextChanged { text, _, _, _ ->
                viewModel.searchNotifications(text.toString())
            }
            setRightDrawableListener {
                text?.clear()
                clearFocus()
                binding.root.hideKeyboard()
                return@setRightDrawableListener true
            }
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::toggleLoading))
            notifications.observe(viewLifecycleOwner, Observer(::updateNotifications))
            notificationsEmpty.observe(
                viewLifecycleOwner,
                Observer(::showNotificationsNotFound)
            )
        }
    }

    private fun scrollToTop() {
        if (binding.notificationsSearchTextView.text.toString().isEmpty()) {
            binding.notificationsRecyclerView.layoutManager?.scrollToPosition(0)
        }
    }

    private fun updateNotifications(notifications: List<NotificationPresentation>) {
        notificationAdapter?.submitList(notifications, ::scrollToTop)
    }

    private fun showNotificationsNotFound(notificationsNotFound: Boolean) {
        binding.notificationsEmptyTextView.animateVisibilty(notificationsNotFound).start()
        binding.notificationsEmptyImageView.animateVisibilty(notificationsNotFound).start()
    }

    private fun openCategoriesFilterDialog() {
        val categoriesFilterDialogFragment = CategoriesFilterDialogFragment()
        categoriesFilterDialogFragment.show(
            childFragmentManager,
            CategoriesFilterDialogFragment.CATEGORIES_FILTER_DIALOG_NAME
        )
    }

    private fun navigateToAnnouncement(announcementId: String) {
        /*val directions = NotificationsFragmentDirections.announcementsToAnnouncement(announcementId)*/
        val bundle = Bundle().apply {
            putString("announcementId", announcementId)
        }
        navController.navigate(R.id.notificationsToAnnouncement, bundle)
    }

    private fun toggleLoading(shouldLoad: Boolean) {
        binding.notificationsSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}