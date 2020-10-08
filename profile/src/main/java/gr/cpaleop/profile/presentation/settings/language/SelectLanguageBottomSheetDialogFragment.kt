package gr.cpaleop.profile.presentation.settings.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import gr.cpaleop.core.domain.behavior.LanguageCode
import gr.cpaleop.profile.R
import gr.cpaleop.profile.databinding.DialogFragmentSelectLanguageBinding
import gr.cpaleop.profile.presentation.ProfileViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseBottomSheetDialog
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SelectLanguageBottomSheetDialogFragment :
    BaseBottomSheetDialog<DialogFragmentSelectLanguageBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentSelectLanguageBinding {
        return DialogFragmentSelectLanguageBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.presentPreferredLanguage()
    }

    private fun observeViewModel() {
        viewModel.run {
            updatedLanguage.observe(viewLifecycleOwner, { restartProcess() })
            selectedLanguage.observe(viewLifecycleOwner, Observer(::updateSelectedLanguage))
        }
    }

    private fun restartProcess() {
        activity?.recreate()
    }

    private fun updateSelectedLanguage(@LanguageCode languageCode: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            when (languageCode) {
                LanguageCode.ENGLISH -> binding.selectLanguageEnglishRadioButton.isChecked = true
                LanguageCode.GREEK -> binding.selectLanguageGreekRadioButton.isChecked = true
            }
            setCheckedChangeListener()
        }
    }

    private fun setCheckedChangeListener() {
        binding.selectLanguageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val language = when (checkedId) {
                R.id.selectLanguageEnglishRadioButton -> LanguageCode.ENGLISH
                R.id.selectLanguageGreekRadioButton -> LanguageCode.GREEK
                else -> return@setOnCheckedChangeListener
            }
            viewModel.updatePreferredLanguage(language)
        }
    }
}