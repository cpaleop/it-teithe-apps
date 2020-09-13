package gr.cpaleop.categoryfilter.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.categoryfilter.R
import gr.cpaleop.categoryfilter.databinding.FragmentCategoryFilterBinding
import gr.cpaleop.categoryfilter.domain.entities.Announcement
import gr.cpaleop.common.OnCompoundDrawableClickListener
import gr.cpaleop.core.presentation.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CategoryFilterFragment : BaseFragment<FragmentCategoryFilterBinding>() {

    private val viewModel: CategoryFilterViewModel by sharedViewModel()
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
        viewModel.presentCategoryName()
        viewModel.presentAnnouncementsByCategory()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews() {
        announcementsAdapter = AnnouncementsAdapter()
        binding.categoryAnnouncementsRecyclerView.adapter = announcementsAdapter

        binding.categoryAnnouncementsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.presentCategoryName()
            viewModel.presentAnnouncementsByCategory()
        }

        binding.categoryAnnouncementsSearchTextView.setOnTouchListener(
            OnCompoundDrawableClickListener(OnCompoundDrawableClickListener.DRAWABLE_LEFT) {
                activity?.onBackPressed()
                return@OnCompoundDrawableClickListener true
            }
        )
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            categoryName.observe(viewLifecycleOwner, Observer(::updateCategoryName))
            announcements.observe(viewLifecycleOwner, Observer(::updateAnnouncements))
        }
    }

    private fun updateCategoryName(categoryName: String) {
        binding.categoryAnnouncementsSearchTextView.hint =
            getString(R.string.category_filter_search_in_hint, categoryName)
    }

    private fun updateAnnouncements(announcements: List<Announcement>) {
        announcementsAdapter?.submitList(announcements)
    }

    private fun updateLoader(shouldLoad: Boolean) {
        binding.categoryAnnouncementsSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}