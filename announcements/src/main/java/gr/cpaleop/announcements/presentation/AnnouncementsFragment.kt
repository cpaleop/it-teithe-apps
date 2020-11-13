package gr.cpaleop.announcements.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import gr.cpaleop.announcements.R
import gr.cpaleop.announcements.databinding.FragmentAnnouncementsBinding
import gr.cpaleop.announcements.di.announcementsModule
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.launch
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import gr.cpaleop.teithe_apps.R as appR

class AnnouncementsFragment :
    BaseApiFragment<FragmentAnnouncementsBinding, AnnouncementsViewModel>(AnnouncementsViewModel::class) {

    private val navController: NavController by lazy { findNavController() }
    private var announcementAdapter: AnnouncementAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAnnouncementsBinding {
        return FragmentAnnouncementsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadKoinModules(announcementsModule)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        unloadKoinModules(announcementsModule)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), appR.color.colorBackground)
        binding.root.hideKeyboard()
        setupPagingAdapter()
        setupViews()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.presentAnnouncements()
    }

    private fun setupPagingAdapter() {
        announcementAdapter =
            AnnouncementAdapter(::navigateToAnnouncement)
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
            doOnTextChanged { text, _, _, _ ->
                viewModel.searchAnnouncements(text.toString())
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
            announcements.observe(viewLifecycleOwner, Observer(::updateAnnouncements))
        }
    }

    private fun navigateToCategoryFilterDialog() {
        val directions = AnnouncementsFragmentDirections.announcementsToCategoryFilterDialog()
        navController.navigate(directions)
    }

    private fun updateAnnouncements(announcements: PagingData<AnnouncementPresentation>) {
        binding.announcementsRecyclerView.post {
            viewLifecycleOwner.lifecycleScope.launch {
                announcementAdapter?.submitData(announcements)
            }
        }
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val bundle = Bundle().apply {
            putString("announcementId", announcementId)
        }
        navController.navigate(R.id.announcementsToAnnouncement, bundle)
    }
}