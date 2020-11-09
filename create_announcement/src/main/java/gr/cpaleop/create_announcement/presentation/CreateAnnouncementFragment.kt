package gr.cpaleop.create_announcement.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import gr.cpaleop.common.extensions.animateVisibiltyWithScale
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.databinding.FragmentCreateAnnouncementBinding
import gr.cpaleop.create_announcement.di.createAnnouncementKoinModule
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CreateAnnouncementFragment :
    BaseApiFragment<FragmentCreateAnnouncementBinding, CreateAnnouncementViewModel>(
        CreateAnnouncementViewModel::class
    ) {

    private val navController: NavController by lazy { findNavController() }
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
        observeViewModel()
    }

    private fun setupViews() {
        createAnnouncementContentStateAdapter = CreateAnnouncementContentStateAdapter(this)
        binding.createAnnouncementContentViewPager.run {
            adapter = createAnnouncementContentStateAdapter
            offscreenPageLimit = 2
        }

        binding.createAnnouncementContentViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.root.hideKeyboard()
            }
        })

        TabLayoutMediator(
            binding.createAnnouncementContentTabLayout,
            binding.createAnnouncementContentViewPager
        ) { tab, position ->
            tab.setText(CreateAnnouncementContentStateAdapter.titles[position])
        }.attach()

        binding.createAnnouncementCategoryTextView.setOnClickListener {
            navigateToCategorySelection()
        }

        binding.createAnnouncementBackImageView.setOnClickListener {
            activity?.finish()
        }

        binding.createAnnouncementSubmitButton.setOnClickListener {
            viewModel.createAnnouncement()
        }

        binding.createAnnouncementAddAttachmentsFab.setOnClickListener {
            navigateToAttachments()
            /*showFileChooser()*/
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            category.observe(viewLifecycleOwner, Observer(::updateSelectedCategory))
            attachmentsCounterVisibility.observe(
                viewLifecycleOwner,
                Observer(::updateAttachmentsCounterBadgeVisibility)
            )
            attachmentsCounter.observe(
                viewLifecycleOwner,
                Observer(::updateAttachmentsCounterBadge)
            )
        }
    }

    private fun updateSelectedCategory(category: Category) {
        binding.createAnnouncementCategoryTextView.run {
            text = category.name
            setCompoundDrawables(null, null, null, null)
        }
    }

    private fun updateAttachmentsCounterBadgeVisibility(shouldShow: Boolean) {
        binding.attachmentsCounterBadgeTextView.animateVisibiltyWithScale(shouldShow).start()
    }

    private fun updateAttachmentsCounterBadge(badgeValue: String) {
        binding.attachmentsCounterBadgeTextView.text = badgeValue
    }

    private fun navigateToAttachments() {
        val directions =
            CreateAnnouncementFragmentDirections.createAnnouncementToAttachments()
        navController.navigate(directions)
    }

    private fun navigateToCategorySelection() {
        val directions =
            CreateAnnouncementFragmentDirections.createAnnouncementToCategorySelectionDialog()
        navController.navigate(directions)
    }
}