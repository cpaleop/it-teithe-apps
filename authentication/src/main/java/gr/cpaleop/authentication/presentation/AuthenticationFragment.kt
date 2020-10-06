package gr.cpaleop.authentication.presentation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.authentication.databinding.FragmentAuthenticationBinding
import gr.cpaleop.core.presentation.BaseApiFragment

class AuthenticationFragment :
    BaseApiFragment<FragmentAuthenticationBinding, AuthenticationViewModel>(AuthenticationViewModel::class) {

    private val navController: NavController by lazy { findNavController() }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAuthenticationBinding {
        return FragmentAuthenticationBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.presentUri()
    }

    private fun observeViewModel() {
        viewModel.run {
            uri.observe(viewLifecycleOwner, Observer(::openCustomTab))
            tokenRetrieved.observe(viewLifecycleOwner, { navigateToDashboard() })
        }
    }

    private fun openCustomTab(uri: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(uri))
    }

    private fun navigateToDashboard() {
        val directions = AuthenticationFragmentDirections.authenticationToDashboard()
        navController.navigate(directions)
        activity?.finishAffinity()
    }
}