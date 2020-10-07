package gr.cpaleop.profile.presentation.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import gr.cpaleop.core.presentation.base.BaseBottomSheetDialog
import gr.cpaleop.profile.databinding.DialogFragmentProfileOptionsBinding
import gr.cpaleop.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfilePersonalOptionsDialogFragment :
    BaseBottomSheetDialog<DialogFragmentProfileOptionsBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()
    private var profileOptionsAdapter: ProfileOptionsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentProfileOptionsBinding {
        return DialogFragmentProfileOptionsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentSocialOptions()
    }

    private fun setupViews() {
        binding.profileOptionsTitleTextView.text =
            navArgs<ProfilePersonalOptionsDialogFragmentArgs>().value.title
        profileOptionsAdapter =
            ProfileOptionsAdapter(::handleChoice)
        binding.profileOptionsRecyclerView.adapter = profileOptionsAdapter
    }

    private fun observeViewModel() {
        viewModel.socialOptions.observe(viewLifecycleOwner, Observer(::updateProfileOptionsList))
    }

    private fun updateProfileOptionsList(profileOptionsList: List<ProfileOption>) {
        profileOptionsAdapter?.submitList(profileOptionsList)
    }

    private fun handleChoice(@StringRes choiceRes: Int) {
        viewModel.handleOptionChoicePersonal(
            choiceRes,
            navArgs<ProfilePersonalOptionsDialogFragmentArgs>().value.type
        )
        dismissAllowingStateLoss()
    }
}