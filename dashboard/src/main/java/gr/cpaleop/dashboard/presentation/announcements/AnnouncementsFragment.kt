package gr.cpaleop.dashboard.presentation.announcements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import gr.cpaleop.common.OnCompoundDrawableClickListener
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.databinding.FragmentAnnouncementsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalPagingApi
class AnnouncementsFragment : BaseFragment<FragmentAnnouncementsBinding>() {

    private val viewModel: AnnouncementsViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }
    private var announcementAdapter: AnnouncementAdapter? = null
    private var hasSearchViewAnimatedToCancel: Boolean = false
    private var hasSearchViewAnimatedToSearch: Boolean = false

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAnnouncementsBinding {
        return FragmentAnnouncementsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.hideKeyboard()
        setupPagingAdapter()
        setupViews()
        observeViewModel()
        viewModel.presentAnnouncements()
    }

    private fun setupPagingAdapter() {
        announcementAdapter = AnnouncementAdapter(::navigateToAnnouncement)
        binding.announcementsRecyclerView.adapter =
            announcementAdapter?.withLoadStateHeaderAndFooter(
                header = AnnouncementsStateAdapter { announcementAdapter?.retry() },
                footer = AnnouncementsStateAdapter { announcementAdapter?.retry() }
            )
        announcementAdapter?.addLoadStateListener { loadState ->
            binding.announcementsSwipeRefreshLayout.isRefreshing =
                loadState.refresh is LoadState.Loading
        }
    }

    private fun setupViews() {
        binding.categoryFilterFab.setOnClickListener {
            navigateToCategoryFilterDialog()
        }

        binding.announcementsSwipeRefreshLayout.setOnRefreshListener {
            announcementAdapter?.refresh()
        }

        binding.annnouncementsSearchTextView.run {
            val endDrawable = AnimatedVectorDrawableCompat.create(
                requireContext(),
                appR.drawable.search_to_cancel
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
                    viewModel.searchAnnouncements(text.toString())

                    var animDrawable: AnimatedVectorDrawableCompat?
                    if (text.isEmpty()) {
                        (compoundDrawables[2] as Animatable2Compat).apply {
                            if (!hasSearchViewAnimatedToSearch) {
                                animDrawable = AnimatedVectorDrawableCompat.create(
                                    requireContext(),
                                    appR.drawable.cancel_to_search
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
                                    appR.drawable.search_to_cancel
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
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            announcements.observe(viewLifecycleOwner, Observer(::updateAnnouncements))
        }
    }

    private fun navigateToCategoryFilterDialog() {
        val directions = AnnouncementsFragmentDirections.announcementsToCategoryFilterDialog()
        navController.navigate(directions)
    }

    private fun updateAnnouncements(announcements: PagingData<AnnouncementPresentation>) {
        viewLifecycleOwner.lifecycleScope.launch {
            announcementAdapter?.submitData(announcements)
        }
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val directions = AnnouncementsFragmentDirections.announcementsToAnnouncement(announcementId)
        navController.navigate(directions)
    }
}