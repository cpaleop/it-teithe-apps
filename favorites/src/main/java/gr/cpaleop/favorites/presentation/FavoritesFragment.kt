package gr.cpaleop.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.favorites.databinding.FragmentFavoritesBinding
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment

class FavoritesFragment :
    BaseApiFragment<FragmentFavoritesBinding, FavoritesViewModel>(FavoritesViewModel::class) {

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

    }

    private fun observeViewModel() {
        viewModel.run {
            announcements.observe(viewLifecycleOwner, Observer { })
        }
    }
}