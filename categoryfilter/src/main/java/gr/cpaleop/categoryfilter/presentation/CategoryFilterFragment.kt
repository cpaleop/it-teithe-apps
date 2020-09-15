package gr.cpaleop.categoryfilter.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import gr.cpaleop.categoryfilter.R
import gr.cpaleop.categoryfilter.databinding.FragmentCategoryFilterBinding
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.common.CompoundDrawableTouchListener
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
@FlowPreview
class CategoryFilterFragment : BaseFragment<FragmentCategoryFilterBinding>() {

    private val viewModel: CategoryFilterViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }
    private var announcementsAdapter: AnnouncementsAdapter? = null
    private var hasSearchViewAnimatedToCancel: Boolean = false
    private var hasSearchViewAnimatedToSearch: Boolean = false
    private var startDrawable: Drawable? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryFilterBinding {
        return FragmentCategoryFilterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.presentCategoryName()
        viewModel.presentAnnouncementsByCategory()
    }

    private fun setupViews() {
        announcementsAdapter = AnnouncementsAdapter(::navigateToAnnouncement)
        binding.categoryAnnouncementsRecyclerView.adapter = announcementsAdapter

        binding.categoryAnnouncementsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.presentCategoryName()
            viewModel.presentAnnouncementsByCategory()
        }

        binding.categoryAnnouncementsSearchTextView.run {
            val endDrawable = AnimatedVectorDrawableCompat.create(
                requireContext(),
                appR.drawable.search_to_cancel
            )

            startDrawable = ContextCompat.getDrawable(
                requireContext(),
                appR.drawable.ic_arrow_back_round
            )

            setCompoundDrawablesWithIntrinsicBounds(
                startDrawable,
                null,
                endDrawable,
                null
            )

            setOnTouchListener(CompoundDrawableTouchListener(
                leftTouchListener = {
                    activity?.onBackPressed()
                    return@CompoundDrawableTouchListener true
                },
                rightTouchListener = {
                    text.clear()
                    clearFocus()
                    binding.root.hideKeyboard()
                    return@CompoundDrawableTouchListener true
                }
            ))

            doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    viewModel.filterAnnouncements(text.toString())

                    var animDrawable: AnimatedVectorDrawableCompat?
                    if (text.isEmpty()) {
                        (compoundDrawables[2] as Animatable2Compat).apply {
                            if (!hasSearchViewAnimatedToSearch) {
                                animDrawable = AnimatedVectorDrawableCompat.create(
                                    requireContext(),
                                    appR.drawable.cancel_to_search
                                )
                                setCompoundDrawablesWithIntrinsicBounds(
                                    startDrawable,
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
                                    startDrawable,
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
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            categoryName.observe(viewLifecycleOwner, Observer(::updateCategoryName))
            announcements.observe(viewLifecycleOwner, Observer(::updateAnnouncements))
            announcementsEmpty.observe(viewLifecycleOwner, Observer(::updateEmptyView))
        }
    }

    private fun updateCategoryName(categoryName: String) {
        binding.categoryAnnouncementsSearchTextView.hint =
            getString(R.string.category_filter_search_in_hint, categoryName)
    }

    private fun updateAnnouncements(announcements: List<Announcement>) {
        announcementsAdapter?.submitList(announcements) {
            binding.categoryAnnouncementsRecyclerView.scrollToPosition(0)
        }
    }

    private fun updateEmptyView(isEmpty: Boolean) {
        binding.categoryAnnouncementsEmptyTextView.isVisible = isEmpty
    }

    private fun updateLoader(shouldLoad: Boolean) {
        binding.categoryAnnouncementsSwipeRefreshLayout.isRefreshing = shouldLoad
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val directions =
            CategoryFilterFragmentDirections.categoryFilterToAnnouncement(announcementId)
        navController.navigate(directions)
    }
}