package gr.cpaleop.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.common.extensions.animateVisibiltyWithScale
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.favorites.R
import gr.cpaleop.favorites.databinding.FragmentFavoritesBinding
import gr.cpaleop.favorites.di.favoritesKoinModule
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import gr.cpaleop.teithe_apps.R as appR

@ExperimentalCoroutinesApi
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadKoinModules(favoritesKoinModule)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        unloadKoinModules(favoritesKoinModule)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), appR.color.colorBackground)
        binding.root.hideKeyboard()
        setupViews()
        observeViewModel()
        viewModel.presentAnnouncements()
    }

    private fun setupViews() {
        favoriteAnnouncementsAdapter = FavoriteAnnouncementsAdapter(::navigateToAnnouncement)
        binding.favoriteAnnouncementsRecyclerView.adapter = favoriteAnnouncementsAdapter

        binding.favoriteAnnnouncementsSearchTextView.run {
            enableLeftDrawable(false)
            doOnTextChanged { text, _, _, _ ->
                viewModel.filter(text.toString())
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
            announcements.observe(viewLifecycleOwner, Observer(::updateFavoriteAnnouncements))
            announcementsEmpty.observe(viewLifecycleOwner, Observer(::updateEmptyViews))
        }
    }

    private fun updateFavoriteAnnouncements(announcementList: List<AnnouncementPresentation>) {
        favoriteAnnouncementsAdapter?.submitList(announcementList)
    }

    private fun updateEmptyViews(shouldShow: Boolean) {
        binding.favoriteAnnouncementsEmptyImageView.animateVisibiltyWithScale(shouldShow).start()
        binding.favoriteAnnouncementsEmptyTextView.animateVisibiltyWithScale(shouldShow).start()
    }

    private fun navigateToAnnouncement(announcementId: String) {
        val bundle = Bundle().apply {
            putString("announcementId", announcementId)
        }
        navController.navigate(R.id.announcementsToAnnouncement, bundle)
    }
}