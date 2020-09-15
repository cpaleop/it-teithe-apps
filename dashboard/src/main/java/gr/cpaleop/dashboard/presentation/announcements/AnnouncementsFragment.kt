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
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentAnnouncementsBinding
import gr.cpaleop.dashboard.presentation.options.OptionsDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalPagingApi
class AnnouncementsFragment : BaseFragment<FragmentAnnouncementsBinding>() {

    private val viewModel: AnnouncementsViewModel by viewModel()
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
        binding.announcementsSortingTextView.setOnClickListener {
            openOptionsDialog()
        }

        binding.categoryFilterFab.setOnClickListener {
            /*openCategoryFilterDialog()*/
            navigateToCategoryFilterDialog()
        }

        binding.announcementsSwipeRefreshLayout.setOnRefreshListener {
            announcementAdapter?.refresh()
        }

        binding.annnouncementsSearchTextView.run {
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
                    viewModel.searchAnnouncements(text.toString())
                    announcementAdapter?.refresh()

                    var animDrawable: AnimatedVectorDrawableCompat?
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
                        binding.annnouncementsSearchTextView.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            searchDrawable,
                            null
                        )
                    } else {
                        binding.annnouncementsSearchTextView.setCompoundDrawablesWithIntrinsicBounds(
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
            announcements.observe(viewLifecycleOwner, Observer(::updateAnnouncements))
        }
    }

    private fun openOptionsDialog() {
        val optionsDialogFragment = OptionsDialogFragment()
        optionsDialogFragment.show(childFragmentManager, OptionsDialogFragment.OPTIONS_DIALOG_NAME)
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