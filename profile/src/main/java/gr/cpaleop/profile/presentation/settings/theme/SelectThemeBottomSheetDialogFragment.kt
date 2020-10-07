package gr.cpaleop.profile.presentation.settings.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import gr.cpaleop.core.presentation.base.BaseBottomSheetDialog
import gr.cpaleop.profile.R
import gr.cpaleop.profile.databinding.DialogFragmentSelectThemeBinding
import gr.cpaleop.profile.presentation.ProfileViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectThemeBottomSheetDialogFragment :
    BaseBottomSheetDialog<DialogFragmentSelectThemeBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentSelectThemeBinding {
        return DialogFragmentSelectThemeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.presentPreferredTheme()
    }

    private fun observeViewModel() {
        viewModel.preferredTheme.observe(viewLifecycleOwner, Observer(::updatePreferredTheme))
        viewModel.updatedTheme.observe(viewLifecycleOwner, Observer(::updateTheme))
    }

    private fun updatePreferredTheme(theme: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            when (theme) {
                AppCompatDelegate.MODE_NIGHT_YES -> binding.selectThemeDarkRadioButton.isChecked =
                    true
                AppCompatDelegate.MODE_NIGHT_NO -> binding.selectThemeLightRadioButton.isChecked =
                    true
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> binding.selectThemeSystemRadioButton.isChecked =
                    true
            }
            setCheckedChangeListener()
        }
    }

    private fun updateTheme(theme: Int) {
        AppCompatDelegate.setDefaultNightMode(theme)
        dismissAllowingStateLoss()
    }

    private fun setCheckedChangeListener() {
        binding.selectThemeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val theme = when (checkedId) {
                R.id.selectThemeLightRadioButton -> AppCompatDelegate.MODE_NIGHT_NO
                R.id.selectThemeDarkRadioButton -> AppCompatDelegate.MODE_NIGHT_YES
                R.id.selectThemeSystemRadioButton -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> return@setOnCheckedChangeListener
            }
            viewModel.updatePreferredTheme(theme)
        }
    }
}