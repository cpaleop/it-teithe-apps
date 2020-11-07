package gr.cpaleop.create_announcement.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import gr.cpaleop.create_announcement.databinding.FragmentCreateAnnouncementBinding
import gr.cpaleop.create_announcement.di.createAnnouncementKoinModule
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CreateAnnouncementFragment :
    BaseApiFragment<FragmentCreateAnnouncementBinding, CreateAnnouncementViewModel>(
        CreateAnnouncementViewModel::class
    ) {

    private var createAnnouncementContentStateAdapter: CreateAnnouncementContentStateAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateAnnouncementBinding {
        return FragmentCreateAnnouncementBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadKoinModules(createAnnouncementKoinModule)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        unloadKoinModules(createAnnouncementKoinModule)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        createAnnouncementContentStateAdapter = CreateAnnouncementContentStateAdapter(this)
        binding.createAnnouncementContentViewPager.run {
            adapter = createAnnouncementContentStateAdapter
            offscreenPageLimit = 2
        }

        TabLayoutMediator(
            binding.createAnnouncementContentTabLayout,
            binding.createAnnouncementContentViewPager
        ) { tab, position ->
            tab.setText(CreateAnnouncementContentStateAdapter.titles[position])
        }.attach()
    }
}