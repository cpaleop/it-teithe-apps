package gr.cpaleop.public_announcements.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.core.presentation.BaseApiFragment
import gr.cpaleop.public_announcements.R
import gr.cpaleop.public_announcements.databinding.FragmentPublicAnnouncementsBinding
import gr.cpaleop.public_announcements.di.PublicAnnouncementsScope
import gr.cpaleop.public_announcements.di.publicAnnouncementsModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.ext.android.getKoin
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

@FlowPreview
@ExperimentalCoroutinesApi
class PublicAnnouncementsFragment :
    BaseApiFragment<FragmentPublicAnnouncementsBinding, PublicAnnouncementsViewModel>(
        PublicAnnouncementsViewModel::class
    ) {

    private val navController: NavController by lazy { findNavController() }
    private var announcementPresentationAdapter: AnnouncementPresentationAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPublicAnnouncementsBinding {
        return FragmentPublicAnnouncementsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadKoinModules(publicAnnouncementsModule)
        getKoin().getOrCreateScope<PublicAnnouncementsScope>(PublicAnnouncementsScope.ID)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        getKoin().getScopeOrNull(PublicAnnouncementsScope.ID)?.close()
        unloadKoinModules(publicAnnouncementsModule)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentAnnouncements()
    }

    private fun setupViews() {
        announcementPresentationAdapter = AnnouncementPresentationAdapter(::navigateToAnnouncement)
        binding.publicAnnouncementsRecyclerView.adapter = announcementPresentationAdapter
        binding.publicAnnouncementsLoginFab.setOnClickListener {
            navigateToAuthentication()
        }

        binding.publicAnnnouncementsSearchTextView.run {
            doOnTextChanged { text, _, _, _ ->
                viewModel.search(text.toString())
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
            announcements.observe(viewLifecycleOwner, Observer(::updateAnnouncements))
            announcementsEmpty.observe(viewLifecycleOwner, Observer(::updateEmptyView))
        }
    }

    private fun navigateToAuthentication() {
        val directions = PublicAnnouncementsFragmentDirections.publicAnnouncementsToAuthentication()
        navController.navigate(directions)
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val bundle = Bundle().apply {
            putString("announcementId", announcementId)
        }
        navController.navigate(R.id.public_announcements_to_announcement, bundle)
    }

    private fun updateAnnouncements(announcementList: List<AnnouncementPresentation>) {
        lifecycleScope.launch {
            announcementPresentationAdapter?.submitList(announcementList) {
                binding.publicAnnouncementsRecyclerView.layoutManager?.scrollToPosition(0)
            }
        }
    }

    private fun updateEmptyView(shouldShow: Boolean) {
        lifecycleScope.launch {
            binding.publicAnnouncementsEmptyTextView.isVisible = shouldShow
        }
    }

    private fun updateLoader(shouldLoad: Boolean) {
        lifecycleScope.launch {
            binding.publicAnnouncementsSwipeRefreshLayout.isRefreshing = shouldLoad
        }
    }
}