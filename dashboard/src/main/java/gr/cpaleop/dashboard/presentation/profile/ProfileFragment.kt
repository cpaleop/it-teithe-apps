package gr.cpaleop.dashboard.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
}