package gr.cpaleop.dashboard.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import coil.api.load
import coil.transform.CircleCropTransformation
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModel()
    private val navController: NavController by lazy { findNavController() }
    private var profileAdapter: ProfileAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.presentProfile()
    }

    private fun setupViews() {
        profileAdapter = ProfileAdapter()
        binding.profileRecyclerView.run {
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            adapter = profileAdapter
        }

        binding.profileSettingsImageView.setOnClickListener {
            val directions = ProfileFragmentDirections.profileToSettings()
            navController.navigate(directions)
        }

        binding.profileSwipeRefreshLayout.setOnRefreshListener {
            viewModel.presentProfile()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::toggleLoading))
            profilePictureUrl.observe(viewLifecycleOwner, Observer(::updateProfilePicture))
            profileDetails.observe(viewLifecycleOwner, Observer(::updateProfileDetails))
        }
    }

    private fun updateProfilePicture(pictureUrl: String) {
        binding.profilePictureImageView.load(pictureUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }

    private fun updateProfileDetails(profileDetails: MutableList<ProfilePresentationDetails>) {
        profileAdapter?.submitList(profileDetails)
    }

    private fun toggleLoading(shouldLoad: Boolean) {
        binding.profileSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}