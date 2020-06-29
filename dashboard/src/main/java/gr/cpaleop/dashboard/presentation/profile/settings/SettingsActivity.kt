package gr.cpaleop.dashboard.presentation.profile.settings

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import gr.cpaleop.core.domain.repositories.PreferencesRepository
import gr.cpaleop.core.presentation.BaseActivity
import gr.cpaleop.dashboard.databinding.ActivitySettingsBinding
import org.koin.android.ext.android.inject

class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

    private val preferencesRepository: PreferencesRepository by inject()

    override fun inflateViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.settingsSwitchThemeButton.setOnClickListener {
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