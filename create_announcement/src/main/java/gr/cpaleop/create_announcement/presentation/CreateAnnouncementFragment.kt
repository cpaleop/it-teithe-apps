package gr.cpaleop.create_announcement.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import gr.cpaleop.common.extensions.animateSoftVisibilty
import gr.cpaleop.common.extensions.animateVisibilty
import gr.cpaleop.common.extensions.animateVisibiltyWithScale
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.create_announcement.databinding.FragmentCreateAnnouncementBinding
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import gr.cpaleop.upload.domain.entities.NewAnnouncement
import gr.cpaleop.upload.domain.entities.UploadProgress
import gr.cpaleop.upload.presentation.UploadAnnouncementWorker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import gr.cpaleop.teithe_apps.R as appR
import gr.cpaleop.upload.R as uploadR

@FlowPreview
@ExperimentalCoroutinesApi
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
            enqueueAnnouncement.observe(viewLifecycleOwner, Observer(::createWorker))
            uploadProgress.observe(viewLifecycleOwner, Observer(::handleProgress))
        }
    }

    private fun handleProgress(uploadProgress: UploadProgress) {
        viewLifecycleOwner.lifecycleScope.launch {
            when (uploadProgress) {
                is UploadProgress.Idle -> {
                }
                is UploadProgress.Uploading -> showProgress(true)
                UploadProgress.Success -> {
                    showProgress(false)
                    showSnackbarMessage(Message(uploadR.string.create_announcement_upload_notification_text_success))
                    delay(2000)
                    activity?.finish()
                }
                is UploadProgress.Failure -> {
                    showProgress(false)
                    showSnackbarMessage(Message(appR.string.error_generic))
                }
            }
        }
    }

    private fun createWorker(newAnnouncement: NewAnnouncement) {
        UploadAnnouncementWorker.enqueue(
            context = requireContext(),
            title = newAnnouncement.title.gr,
            titleEn = newAnnouncement.title.en,
            text = newAnnouncement.text.gr,
            textEn = newAnnouncement.title.en,
            categoryId = newAnnouncement.category,
            attachmentUriList = newAnnouncement.attachmentsUriList.toTypedArray()
        )
    }

    private fun showProgress(shouldShow: Boolean) {
        binding.createAnnouncementCoverView.animateVisibilty(shouldShow).start()
        binding.createAnnouncementUploadProgressBar.animateSoftVisibilty(shouldShow).start()
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
        val extras =
            FragmentNavigatorExtras(
                binding.createAnnouncementAddAttachmentsFab
                        to binding.createAnnouncementAddAttachmentsFab.transitionName
            )
        navController.navigate(directions, extras)
    }

    private fun navigateToCategorySelection() {
        val directions =
            CreateAnnouncementFragmentDirections.createAnnouncementToCategorySelectionDialog()
        navController.navigate(directions)
    }
}