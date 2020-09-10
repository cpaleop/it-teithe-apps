package gr.cpaleop.dashboard.presentation.notifications

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentNotificationsBinding
import gr.cpaleop.dashboard.presentation.OnCompoundDrawableClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import gr.cpaleop.teithe_apps.R as appR

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
        binding.root.hideKeyboard()
        setupViews()
        observeViewModel()
        viewModel.presentNotifications()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews() {
        notificationAdapter = NotificationAdapter(::navigateToAnnouncement)
        binding.notificationsRecyclerView.adapter = notificationAdapter

        binding.notificationsSwipeRefreshLayout.setOnRefreshListener {
            binding.notificationsSearchTextView.setText(requireContext().getString(appR.string.empty))
            viewModel.presentNotifications()
        }

        binding.notificationsSearchTextView.run {
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    this.animate().scaleXBy(0.03f).scaleYBy(0.03f).start()
                } else {
                    this.animate().scaleXBy(-0.03f).scaleYBy(-0.03f).start()
                }
            }
            setOnTouchListener(
                OnCompoundDrawableClickListener(OnCompoundDrawableClickListener.DRAWABLE_RIGHT) {
                    binding.notificationsSearchTextView.text.clear()
                }
            )

            doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    viewModel.searchNotifications(text.toString())

                    val searchDrawable = requireContext().getDrawable(R.drawable.ic_search)
                    val clearDrawable = requireContext().getDrawable(R.drawable.ic_close)
                    if (text.isEmpty()) {
                        binding.notificationsSearchTextView.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            searchDrawable,
                            null
                        )
                    } else {
                        binding.notificationsSearchTextView.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            clearDrawable,
                            null
                        )
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::toggleLoading))
            notifications.observe(viewLifecycleOwner, Observer(::updateNotifications))
            notificationsEmpty.observe(viewLifecycleOwner, Observer(::showNotificationsEmpty))
            notificationsFilterEmpty.observe(
                viewLifecycleOwner,
                Observer(::showNotificationsNotFound)
            )
        }
    }

    private fun updateNotifications(notifications: List<NotificationPresentation>) {
        notificationAdapter?.submitList(notifications) {
            binding.notificationsRecyclerView.scrollToPosition(0)
        }
    }

    private fun showNotificationsEmpty(notificationsEmpty: Boolean) {
        binding.notificationsEmptyTextView.run {
            text = requireContext().getString(R.string.notifications_empty)
            isVisible = notificationsEmpty
        }
    }

    private fun showNotificationsNotFound(notificationsNotFound: Boolean) {
        binding.notificationsEmptyTextView.run {
            text = requireContext().getString(R.string.notifications_not_found)
            isVisible = notificationsNotFound
        }
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val directions = NotificationsFragmentDirections.announcementsToAnnouncement(announcementId)
        navController.navigate(directions)
    }

    private fun toggleLoading(shouldLoad: Boolean) {
        binding.notificationsSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}