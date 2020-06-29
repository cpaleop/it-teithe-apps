package gr.cpaleop.dashboard.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import coil.api.load
import coil.transform.CircleCropTransformation
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.databinding.FragmentProfileBinding
import gr.cpaleop.dashboard.domain.entities.Profile
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.presentProfile()
    }

    private fun observeViewModel() {
        viewModel.run {
            profile.observe(viewLifecycleOwner, Observer(::updateProfile))
        }
    }

    private fun updateProfile(profile: Profile) {
        binding.profilePictureImageView.load(profile.profileImageUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }
}