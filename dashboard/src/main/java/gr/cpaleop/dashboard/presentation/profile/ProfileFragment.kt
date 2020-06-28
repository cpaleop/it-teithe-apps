package gr.cpaleop.dashboard.presentation.profile

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.databinding.FragmentProfileBinding
import org.koin.android.ext.android.inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val preferencesRepository: PreferencesRepository by inject()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchThemeButton.setOnClickListener {
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    preferencesRepository.putInt(
                        PreferencesRepository.NIGHT_MODE,
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    preferencesRepository.putInt(
                        PreferencesRepository.NIGHT_MODE,
                        AppCompatDelegate.MODE_NIGHT_NO
                    )
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    preferencesRepository.putInt(
                        PreferencesRepository.NIGHT_MODE,
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }
}