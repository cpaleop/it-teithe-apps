package gr.cpaleop.profile.presentation.socials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.profile.databinding.FragmentProfileSocialsBinding
import gr.cpaleop.profile.presentation.ProfileFragmentDirections
import gr.cpaleop.profile.presentation.ProfileSocialDetails
import gr.cpaleop.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileSocialsFragment : BaseFragment<FragmentProfileSocialsBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }
    private var profileSocialsAdapter: ProfileSocialsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileSocialsBinding {
        return FragmentProfileSocialsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        profileSocialsAdapter = ProfileSocialsAdapter(::navigateToProfileOptionsDialogFragment)
        binding.profileRecyclerView.adapter = profileSocialsAdapter
    }

    private fun observeViewModel() {
        viewModel.profileSocials.observe(viewLifecycleOwner, Observer(::updateSocials))
    }

    private fun updateSocials(socialList: List<ProfileSocialDetails>) {
        profileSocialsAdapter?.submitList(socialList)
    }

    private fun navigateToProfileOptionsDialogFragment(title: String) {
        val directions = ProfileFragmentDirections.profileToProfileOptionsDialog("social", title)
        navController.navigate(directions)
    }
}