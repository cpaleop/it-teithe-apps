package gr.cpaleop.create_announcement.presentation.attachments

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import gr.cpaleop.common.extensions.animateVisibiltyWithScale
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.core.presentation.file_chooser.FileChooser
import gr.cpaleop.create_announcement.R
import gr.cpaleop.create_announcement.databinding.FragmentAttachmentsBinding
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementActivity
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import gr.cpaleop.teithe_apps.R as appR

@FlowPreview
@ExperimentalCoroutinesApi
class AttachmentsFragment : BaseApiFragment<FragmentAttachmentsBinding,
        CreateAnnouncementViewModel>(CreateAnnouncementViewModel::class) {

    private val fileChooser: FileChooser by inject()
    private var attachmentPresentationAdapter: AttachmentPresentationAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAttachmentsBinding {
        return FragmentAttachmentsBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = null
        exitTransition = null
        reenterTransition = null
        returnTransition = null
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = resources.getInteger(appR.integer.shared_animation_duration).toLong()
            scrimColor = ContextCompat.getColor(requireContext(), appR.color.colorBackground)
            containerColor = ContextCompat.getColor(requireContext(), appR.color.colorBackground)
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
            isElevationShadowEnabled = false
            this.pathMotion = MaterialArcMotion()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.hideKeyboard()
        setupViews()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.presentAttachments()
    }

    private fun setupViews() {
        attachmentPresentationAdapter = AttachmentPresentationAdapter(viewModel::removeAttachment)
        binding.attachmentsRecyclerView.adapter = attachmentPresentationAdapter

        binding.attachmentsBackImageView.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.attachmentsAddFab.setOnClickListener {
            showFileChooser()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            attachments.observe(viewLifecycleOwner, Observer(::updateAttachments))
            attachmentsEmpty.observe(viewLifecycleOwner, Observer(::updateAttachmentsEmptyViews))
        }
    }

    private fun updateAttachments(attachmentList: List<AttachmentPresentation>) {
        attachmentPresentationAdapter?.submitList(attachmentList)
    }

    private fun updateAttachmentsEmptyViews(shouldShow: Boolean) {
        binding.attachmentsEmptyImageView.animateVisibiltyWithScale(shouldShow).start()
        binding.attachmentsEmptyTextView.animateVisibiltyWithScale(shouldShow).start()
    }

    private fun showFileChooser() {
        try {
            fileChooser(activity ?: return, CreateAnnouncementActivity.CODE_FILE_SELECTION)
        } catch (ex: ActivityNotFoundException) {
            showSnackbarMessage(Message(R.string.create_announcement_install_file_manager))
        }
    }
}