package gr.cpaleop.dashboard.presentation.announcements

import android.annotation.SuppressLint
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
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentAnnouncementsBinding
import gr.cpaleop.dashboard.presentation.OnCompoundDrawableClickListener
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalPagingApi
class AnnouncementsFragment : BaseFragment<FragmentAnnouncementsBinding>() {

    private val viewModel: AnnouncementsViewModel by viewModel()
    private val navController: NavController by lazy { findNavController() }
    private var announcementAdapter: AnnouncementAdapter? = null

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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViews() {
        binding.announcementsSwipeRefreshLayout.setOnRefreshListener {
            announcementAdapter?.refresh()
        }

        binding.annnouncementsSearchTextView.setOnTouchListener(
            OnCompoundDrawableClickListener(OnCompoundDrawableClickListener.DRAWABLE_RIGHT) {
                binding.annnouncementsSearchTextView.text.clear()
            }
        )

        binding.annnouncementsSearchTextView.run {
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    this.animate().scaleXBy(0.03f).scaleYBy(0.03f).start()
                } else {
                    this.animate().scaleXBy(-0.03f).scaleYBy(-0.03f).start()
                }
            }

            doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    viewModel.searchAnnouncements(text.toString())

                    val searchDrawable = requireContext().getDrawable(R.drawable.ic_search)
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