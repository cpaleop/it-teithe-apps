package gr.cpaleop.dashboard.presentation.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentProfileBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()
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
        profileAdapter = ProfileAdapter(::navigateToProfileOptionsDialogFragment)
        binding.profileRecyclerView.run {
            layoutManager = GridLayoutManager(requireContext(), 2)
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
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            profile.observe(viewLifecycleOwner, Observer(::updateProfileDetails))
            choiceEdit.observe(viewLifecycleOwner, Observer(::editSocial))
            choiceCopyToClipboard.observe(viewLifecycleOwner, Observer(::copyToClipboard))
        }
    }

    private fun updateProfileDetails(profilePresentation: ProfilePresentation) {
        binding.profilePictureImageView.load(profilePresentation.profilePhotoUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.profileAmTextView.visibility = View.VISIBLE
        binding.profileEmailTextView.visibility = View.VISIBLE
        binding.profileUsernameTextView.visibility = View.VISIBLE
        binding.profileSemesterTextView.visibility = View.VISIBLE
        binding.profileRegisteredYearTextView.visibility = View.VISIBLE

        binding.profileAmValueTextView.text = profilePresentation.am
        binding.profileEmailValueTextView.text = profilePresentation.email
        binding.profileUsernameValueTextView.text = profilePresentation.username
        binding.profileDisplayNameTextView.text = profilePresentation.displayName
        binding.profileSemesterValueTextView.text = profilePresentation.semester
        binding.profileRegisteredYearValueTextView.text = profilePresentation.registeredYear
        profileAdapter?.submitList(profilePresentation.social)
    }

    private fun updateLoader(shouldLoad: Boolean) {
        binding.profileSwipeRefreshLayout.isRefreshing = shouldLoad
    }

    private fun navigateToProfileOptionsDialogFragment(title: String) {
        val directions = ProfileFragmentDirections.profileToProfileOptionsDialog(title)
        navController.navigate(directions)
    }

    private fun copyToClipboard(selectedSocialOption: SelectedSocialOption) {
        val clipboard =
            activity?.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(selectedSocialOption.title, selectedSocialOption.value)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            activity?.applicationContext,
            getString(R.string.profile_toast_clipboard),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun editSocial(selectedSocialOption: SelectedSocialOption) {
        TODO("Show dialog to rename")
    }
}