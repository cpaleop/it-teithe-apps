package gr.cpaleop.categoryfilter.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.categoryfilter.R
import gr.cpaleop.categoryfilter.databinding.FragmentCategoryFilterBinding
import gr.cpaleop.common.extensions.animateVisibiltyWithScale
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class CategoryFilterFragment :
    BaseApiFragment<FragmentCategoryFilterBinding, CategoryFilterViewModel>(CategoryFilterViewModel::class) {

    private val navController: NavController by lazy { findNavController() }
    private var announcementsAdapter: AnnouncementsAdapter? = null

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
        viewModel.presentAnnouncements()
    }

    private fun setupViews() {
        announcementsAdapter = AnnouncementsAdapter(::navigateToAnnouncement)
        binding.categoryAnnouncementsRecyclerView.adapter = announcementsAdapter

        binding.categoryAnnouncementsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.presentCategoryName()
            viewModel.presentAnnouncements()
        }

        binding.categoryAnnouncementsSearchTextView.run {
            enableLeftDrawable(true)
            doOnTextChanged { text, _, _, _ ->
                viewModel.filterAnnouncements(text.toString())
            }
            setLeftDrawableListener {
                activity?.onBackPressed()
                return@setLeftDrawableListener true
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
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            categoryName.observe(viewLifecycleOwner, Observer(::updateCategoryName))
            announcements.observe(viewLifecycleOwner, Observer(::updateAnnouncements))
            announcementsEmpty.observe(viewLifecycleOwner, Observer(::updateEmptyView))
        }
    }

    private fun scrollToTop() {
        if (binding.categoryAnnouncementsSearchTextView.text.toString().isEmpty()) {
            binding.categoryAnnouncementsRecyclerView.layoutManager?.scrollToPosition(0)
        }
    }

    private fun updateCategoryName(categoryName: String) {
        binding.categoryAnnouncementsSearchTextView.hint =
            getString(R.string.category_filter_search_hint, categoryName)
    }

    private fun updateAnnouncements(announcements: List<AnnouncementPresentation>) {
        announcementsAdapter?.submitList(announcements, ::scrollToTop)
    }

    private fun updateEmptyView(isEmpty: Boolean) {
        binding.categoryAnnouncementsEmptyTextView.animateVisibiltyWithScale(isEmpty).start()
        binding.categoryAnnouncementsEmptyImageView.animateVisibiltyWithScale(isEmpty).start()
    }

    private fun updateLoader(shouldLoad: Boolean) {
        binding.categoryAnnouncementsSwipeRefreshLayout.isRefreshing = shouldLoad
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val bundle = Bundle().apply {
            putString("announcementId", announcementId)
        }
        navController.navigate(R.id.categoryFilterToAnnouncement, bundle)
    }
}