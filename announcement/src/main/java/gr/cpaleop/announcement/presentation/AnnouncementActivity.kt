package gr.cpaleop.announcement.presentation

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import gr.cpaleop.announcement.databinding.ActivityAnnouncementBinding
import gr.cpaleop.announcement.di.announcementModule
import gr.cpaleop.core.presentation.BaseActivity
import gr.cpaleop.download.presentation.DownloadFileWorker
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import gr.cpaleop.teithe_apps.R as appR

class AnnouncementActivity : BaseActivity<ActivityAnnouncementBinding>() {

    private val viewModel: AnnouncementViewModel by viewModel()
    private var announcementId: String = ""

    override fun inflateViewBinding(): ActivityAnnouncementBinding {
        return ActivityAnnouncementBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(announcementModule)
        super.onCreate(savedInstanceState)
        handleIntent()
        setupViews()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.presentAnnouncement(announcementId)
    }

    override fun onDestroy() {
        unloadKoinModules(announcementModule)
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(appR.anim.fade_in, appR.anim.fade_out)
    }

    private fun handleIntent() {
        announcementId = intent.getStringExtra(ARG_ANNOUNCEMENT_ID) ?: ""
    }

    private fun setupViews() {
        binding.announcementBackButton.setOnClickListener {
            onBackPressed()
        }

        binding.announcementDownloadAttachmentButton.setOnClickListener {
            viewModel.downloadAttachments(announcementId)
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            val lifecycleOwner = this@AnnouncementActivity
            announcement.observe(lifecycleOwner, Observer(::updateAnnouncement))
            downloadStatus.observe(lifecycleOwner, Observer(::updateDownloadStatus))
            attachmentFileId.observe(lifecycleOwner, Observer(::initiateDownload))
        }
    }

    private fun updateDownloadStatus(isDownloading: Boolean) {
        binding.announcementDownloadAttachmentButton.isVisible = !isDownloading
        binding.announcementDownloadAttachmentProgress.isVisible = isDownloading
    }

    private fun updateAnnouncement(announcement: AnnouncementDetails) {
        binding.announcementTitle.text = announcement.title
        binding.announcementDate.text = announcement.date
        binding.announcementPublisher.text = announcement.publisherName
        binding.announcementCategory.run {
            text = announcement.category
            isVisible = announcement.category.isNotEmpty()
        }
        binding.announcementContent.text = announcement.text
        binding.announcementDownloadAttachmentButton.isVisible =
            announcement.attachments.isNotEmpty()
    }

    private fun initiateDownload(announcementDocument: AnnouncementDocument) {
        DownloadFileWorker.enqueue(
            applicationContext,
            announcementDocument.announcementId,
            announcementDocument.fileIdList.toTypedArray()
        )
    }

    companion object {

        private const val ARG_ANNOUNCEMENT_ID = "announcementId"
    }
}