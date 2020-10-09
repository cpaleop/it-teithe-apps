package gr.cpaleop.profile.presentation.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.profile.databinding.FragmentProfilePersonalBinding
import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.presentation.ProfileFragmentDirections
import gr.cpaleop.profile.presentation.ProfilePersonalDetails
import gr.cpaleop.profile.presentation.ProfileViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfilePersonalDetailsFragment : BaseFragment<FragmentProfilePersonalBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }
    private var profilePersonalAdapter: ProfilePersonalAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfilePersonalBinding {
        return FragmentProfilePersonalBinding.inflate(inflater, container, false)
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
        profilePersonalAdapter = ProfilePersonalAdapter(::navigateToProfileOptionsDialogFragment)
        binding.profilePersonalDetailsRecyclerView.adapter = profilePersonalAdapter
    }

    private fun observeViewModel() {
        viewModel.profilePersonalDetails.observe(
            viewLifecycleOwner,
            Observer(::updatePersonalDetails)
        )
    }

    private fun updatePersonalDetails(personalList: List<ProfilePersonalDetails>) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            binding.profilePersonalDetailsRecyclerView.post {
                profilePersonalAdapter?.submitList(personalList)
            }
        }
    }

    private fun navigateToProfileOptionsDialogFragment(title: String, personalType: Personal) {
        val directions =
            ProfileFragmentDirections.profileToProfilePersonalOptionsDialog(title, personalType)
        navController.navigate(directions)
    }
}