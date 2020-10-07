package gr.cpaleop.teithe_apps.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.core.presentation.base.BaseApiFragment
import gr.cpaleop.teithe_apps.databinding.FragmentSplashBinding

class SplashFragment :
    BaseApiFragment<FragmentSplashBinding, SplashViewModel>(SplashViewModel::class) {

    private val navController: NavController by lazy { findNavController() }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.checkUserAuthentication()
    }

    private fun observeViewModel() {
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner, Observer(::handleUserNavigation))
    }

    private fun handleUserNavigation(isLoggedIn: Boolean) {
        if (!isLoggedIn) {
            val directions = SplashFragmentDirections.splashToPublicAnnouncements()
            navController.navigate(directions)
        } else {
            val directions = SplashFragmentDirections.splashToDashboard()
            navController.navigate(directions)
            activity?.finishAffinity()
        }
    }
}