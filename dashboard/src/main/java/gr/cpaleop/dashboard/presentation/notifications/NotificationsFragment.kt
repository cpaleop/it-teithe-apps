package gr.cpaleop.dashboard.presentation.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import gr.cpaleop.common.OnCompoundDrawableClickListener
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentNotificationsBinding
import gr.cpaleop.dashboard.presentation.notifications.categories.CategoriesFilterDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import gr.cpaleop.teithe_apps.R as appR

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    private val viewModel: NotificationsViewModel by viewModel()
    private val navController: NavController by lazy { findNavController() }
    private var notificationAdapter: NotificationAdapter? = null
    private var hasSearchViewAnimatedToCancel: Boolean = false
    private var hasSearchViewAnimatedToSearch: Boolean = false

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
            val endDrawable = AnimatedVectorDrawableCompat.create(
                requireContext(),
                R.drawable.search_to_cancel
            )
            setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                endDrawable,
                null
            )

            setOnTouchListener(
                OnCompoundDrawableClickListener(OnCompoundDrawableClickListener.DRAWABLE_RIGHT) {
                    text.clear()
                    clearFocus()
                    binding.root.hideKeyboard()
                    return@OnCompoundDrawableClickListener true
                }
            )

            doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    viewModel.searchNotifications(text.toString())

                    var animDrawable: AnimatedVectorDrawableCompat? = null
                    if (text.isEmpty()) {
                        (compoundDrawables[2] as Animatable2Compat).apply {
                            if (!hasSearchViewAnimatedToSearch) {
                                animDrawable = AnimatedVectorDrawableCompat.create(
                                    requireContext(),
                                    R.drawable.cancel_to_search
                                )
                                setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    animDrawable,
                                    null
                                )

                                animDrawable?.start()
                                hasSearchViewAnimatedToCancel = false
                                hasSearchViewAnimatedToSearch = !hasSearchViewAnimatedToSearch
                            }
                        }
                    } else {
                        (compoundDrawables[2] as Animatable2Compat).apply {
                            if (!hasSearchViewAnimatedToCancel) {
                                animDrawable = AnimatedVectorDrawableCompat.create(
                                    requireContext(),
                                    R.drawable.search_to_cancel
                                )
                                setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    animDrawable,
                                    null
                                )

                                animDrawable?.start()
                                hasSearchViewAnimatedToSearch = false
                                hasSearchViewAnimatedToCancel = !hasSearchViewAnimatedToCancel
                            }
                        }
                    }

                    /*val searchDrawable = requireContext().getDrawable(R.drawable.ic_search)
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
                    }*/
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

    private fun openCategoriesFilterDialog() {
        val categoriesFilterDialogFragment = CategoriesFilterDialogFragment()
        categoriesFilterDialogFragment.show(
            childFragmentManager,
            CategoriesFilterDialogFragment.CATEGORIES_FILTER_DIALOG_NAME
        )
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val directions = NotificationsFragmentDirections.announcementsToAnnouncement(announcementId)
        navController.navigate(directions)
    }

    private fun toggleLoading(shouldLoad: Boolean) {
        binding.notificationsSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}