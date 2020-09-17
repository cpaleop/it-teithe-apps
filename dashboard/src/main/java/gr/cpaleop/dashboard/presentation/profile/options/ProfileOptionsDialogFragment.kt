package gr.cpaleop.dashboard.presentation.profile.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.common.extensions.setLifecycleOwner
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentProfileOptionsBinding
import gr.cpaleop.dashboard.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileOptionsDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: ProfileViewModel by sharedViewModel()
    private var _binding: DialogFragmentProfileOptionsBinding? = null
    private val binding: DialogFragmentProfileOptionsBinding get() = _binding!!
    private var profileOptionsAdapter: ProfileOptionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_profile_options, container, false)
        _binding =
            DialogFragmentProfileOptionsBinding.bind(view).setLifecycleOwner(viewLifecycleOwner)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentSocialOptions()
    }

    private fun setupViews() {
        binding.profileOptionsTitleTextView.text =
            navArgs<ProfileOptionsDialogFragmentArgs>().value.socialName
        profileOptionsAdapter = ProfileOptionsAdapter(::handleChoice)
        binding.profileOptionsRecyclerView.adapter = profileOptionsAdapter
    }

    private fun observeViewModel() {
        viewModel.socialOptions.observe(viewLifecycleOwner, Observer(::updateProfileOptionsList))
    }

    private fun updateProfileOptionsList(profileOptionsList: List<ProfileOption>) {
        profileOptionsAdapter?.submitList(profileOptionsList)
    }

    private fun handleChoice(choice: String) {
        viewModel.handleOptionChoice(
            choice,
            navArgs<ProfileOptionsDialogFragmentArgs>().value.socialName
        )
        dismissAllowingStateLoss()
    }
}