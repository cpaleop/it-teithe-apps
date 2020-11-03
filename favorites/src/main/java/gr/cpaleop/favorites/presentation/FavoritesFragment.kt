package gr.cpaleop.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.favorites.R
import gr.cpaleop.favorites.databinding.FragmentFavoritesBinding
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment

class FavoritesFragment :
    BaseApiFragment<FragmentFavoritesBinding, FavoritesViewModel>(FavoritesViewModel::class) {

    private val navController: NavController by lazy { findNavController() }
    private var favoriteAnnouncementsAdapter: FavoriteAnnouncementsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding {
        return FragmentFavoritesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentAnnouncements()
    }

    private fun setupViews() {
        favoriteAnnouncementsAdapter = FavoriteAnnouncementsAdapter(::navigateToAnnouncement)
        binding.favoriteAnnouncementsRecyclerView.adapter = favoriteAnnouncementsAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            announcements.observe(viewLifecycleOwner, Observer(::updateFavoriteAnnouncements))
        }
    }

    private fun updateFavoriteAnnouncements(announcementList: List<AnnouncementPresentation>) {
        favoriteAnnouncementsAdapter?.submitList(announcementList)
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val bundle = Bundle().apply {
            putString("announcementId", announcementId)
        }
        navController.navigate(R.id.announcementsToAnnouncement, bundle)
    }
}