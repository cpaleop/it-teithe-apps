package gr.cpaleop.profile.presentation.settings.change_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import gr.cpaleop.profile.R
import gr.cpaleop.profile.databinding.DialogFragmentChangePasswordBinding
import gr.cpaleop.profile.presentation.ProfileViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseBottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ChangePasswordDialogFragment : BaseBottomSheetDialog<DialogFragmentChangePasswordBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentChangePasswordBinding {
        return DialogFragmentChangePasswordBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.profileChangePasswordSubmitButton.setOnClickListener {
            viewModel.changePassword(
                oldPassword = binding.profileChangePasswordOldPasswordEditText.text.toString(),
                newPassword = binding.profileChangePasswordNewPasswordEditText.text.toString()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            invalidPassword.observe(viewLifecycleOwner, Observer(::updateInvalidPasswordView))
            changePasswordSuccess.observe(viewLifecycleOwner, { dismissAllowingStateLoss() })
        }
    }

    private fun updateInvalidPasswordView(invalidPassword: Boolean) {
        binding.profileChangePasswordOldPasswordInputLayout.run {
            isErrorEnabled = invalidPassword
            error = requireContext().getString(R.string.profile_settings_change_password_invalid)
        }

        binding.profileChangePasswordNewPasswordInputLayout.run {
            isErrorEnabled = invalidPassword
            error = requireContext().getString(R.string.profile_settings_change_password_invalid)
        }
    }

    private fun updateLoader(shouldLoad: Boolean) {
        binding.profileChangePasswordProgressBar.isVisible = shouldLoad
    }
}