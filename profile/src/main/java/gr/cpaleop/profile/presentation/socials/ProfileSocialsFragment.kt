package gr.cpaleop.profile.presentation.socials

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.profile.R
import gr.cpaleop.profile.databinding.FragmentProfileSocialsBinding
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.presentation.ProfileFragmentDirections
import gr.cpaleop.profile.presentation.ProfileSocialDetails
import gr.cpaleop.profile.presentation.ProfileViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileSocialsFragment :
    BaseApiFragment<FragmentProfileSocialsBinding, ProfileViewModel>(ProfileViewModel::class) {

    private val navController: NavController by lazy { findNavController() }
    private var profileSocialsAdapter: ProfileSocialsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileSocialsBinding {
        return FragmentProfileSocialsBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = null
        returnTransition = null
        exitTransition = null
        reenterTransition = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        profileSocialsAdapter =
            ProfileSocialsAdapter(::openSocial, ::navigateToProfileOptionsDialogFragment)
        binding.profileRecyclerView.adapter = profileSocialsAdapter
    }

    private fun observeViewModel() {
        viewModel.profileSocials.observe(viewLifecycleOwner, Observer(::updateSocials))
    }

    private fun updateSocials(socialList: List<ProfileSocialDetails>) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            binding.profileRecyclerView.post {
                profileSocialsAdapter?.submitList(socialList)
            }
        }
    }

    private fun openSocial(socialUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(socialUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (t: ActivityNotFoundException) {
            Timber.e(t)
            showSnackbarMessage(Message(R.string.profile_socials_navigate_error))
        }
    }

    private fun navigateToProfileOptionsDialogFragment(title: String, socialType: Social) {
        val directions =
            ProfileFragmentDirections.profileToProfileSocialOptionsDialog(title, socialType)
        navController.navigate(directions)
    }
}