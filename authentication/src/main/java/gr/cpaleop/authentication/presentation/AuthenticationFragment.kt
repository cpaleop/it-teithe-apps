package gr.cpaleop.authentication.presentation

import android.content.ComponentName
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.authentication.databinding.FragmentAuthenticationBinding
import gr.cpaleop.core.presentation.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AuthenticationFragment : BaseFragment<FragmentAuthenticationBinding>() {

    private val viewModel: AuthenticationViewModel by sharedViewModel()
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
            tokenRetrieved.observe(viewLifecycleOwner, Observer { navigateToDashboard() })
        }
    }

    private fun openCustomTab(uri: Uri) {
        val connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                client: CustomTabsClient
            ) {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                client.warmup(0L)
                customTabsIntent.launchUrl(requireContext(), uri)
            }

            override fun onServiceDisconnected(p0: ComponentName?) {}
        }

        CustomTabsClient.bindCustomTabsService(requireContext(), "com.android.chrome", connection)
    }

    private fun navigateToDashboard() {
        //TODO("Navigate to Dashboard")
    }
}