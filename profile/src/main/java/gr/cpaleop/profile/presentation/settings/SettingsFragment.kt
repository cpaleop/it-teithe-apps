package gr.cpaleop.profile.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.profile.R
import gr.cpaleop.profile.databinding.FragmentSettingsBinding
import gr.cpaleop.profile.presentation.ProfileFragmentDirections
import gr.cpaleop.profile.presentation.ProfileViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: ProfileViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }
    private var settingsAdapter: SettingsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentSettings()
    }

    private fun setupViews() {
        settingsAdapter = SettingsAdapter(::handleOption)
        binding.profileSettingsRecyclerView.adapter = settingsAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            settings.observe(viewLifecycleOwner, Observer(::updateSettings))
            logoutSuccess.observe(viewLifecycleOwner, { navigateToSplash() })
        }
    }

    private fun updateSettings(settingsList: List<Setting>) {
        viewLifecycleOwner.lifecycleScope.launch {
            settingsAdapter?.submitList(settingsList)
        }
    }

    private fun handleOption(@StringRes title: Int) {
        when (title) {
            R.string.profile_settings_change_theme -> navigateToSelectThemeDialog()
            R.string.profile_settings_change_language -> navigateToSelectLanguageDialog()
            R.string.profile_settings_logout -> viewModel.logout()
        }
    }

    private fun navigateToSelectLanguageDialog() {
        val directions = ProfileFragmentDirections.profileToSelectLanguageDialog()
        navController.navigate(directions)
    }

    private fun navigateToSelectThemeDialog() {
        val directions = ProfileFragmentDirections.profileToSelectThemeDialog()
        navController.navigate(directions)
    }

    private fun navigateToSplash() {
        val directions = ProfileFragmentDirections.profileToSplash()
        navController.navigate(directions)
    }
}