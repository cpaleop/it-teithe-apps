package gr.cpaleop.public_announcements.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.public_announcements.databinding.FragmentPublicAnnouncementsBinding
import gr.cpaleop.public_announcements.di.publicAnnouncementsModule
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class PublicAnnouncementsFragment : BaseFragment<FragmentPublicAnnouncementsBinding>() {

    private val viewModel: PublicAnnouncementsViewModel by viewModel()
    private val navController: NavController by lazy { findNavController() }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPublicAnnouncementsBinding {
        return FragmentPublicAnnouncementsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadKoinModules(publicAnnouncementsModule)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        unloadKoinModules(publicAnnouncementsModule)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.publicAnnouncementsLoginFab.setOnClickListener {
            navigateToAuthentication()
        }
    }

    private fun observeViewModel() {
        viewModel.run {

        }
    }

    private fun navigateToAuthentication() {
        /*val directions = PublicAnnouncementsFragmentDirections.publicAnnouncementsToAuthentication()
        navController.navigate(directions)*/
    }
}