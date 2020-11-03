package gr.cpaleop.announcement.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import gr.cpaleop.announcement.databinding.ActivityAnnouncementBinding
import gr.cpaleop.announcement.di.announcementModule
import gr.cpaleop.download.presentation.DownloadFileWorker
import gr.cpaleop.teithe_apps.presentation.base.BaseApiActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import gr.cpaleop.teithe_apps.R as appR

@FlowPreview
@ExperimentalCoroutinesApi
class AnnouncementActivity :
    BaseApiActivity<ActivityAnnouncementBinding, AnnouncementViewModel>(AnnouncementViewModel::class) {

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
        overridePendingTransition(appR.anim.pop_enter_fade_in, appR.anim.fade_out)
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

        binding.announcementFavoriteImageView.setOnClickListener {
            viewModel.favoriteAnnouncement(announcementId)
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            val lifecycleOwner = this@AnnouncementActivity
            announcement.observe(lifecycleOwner, Observer(::updateAnnouncement))
            isFavorite.observe(lifecycleOwner, Observer(::updateFavorite))
            downloadStatus.observe(lifecycleOwner, Observer(::updateDownloadStatus))
            attachmentFileId.observe(lifecycleOwner, Observer(::initiateDownload))
        }
    }

    private fun updateDownloadStatus(isDownloading: Boolean) {
        binding.announcementDownloadAttachmentButton.visibility =
            if (!isDownloading) View.VISIBLE else View.INVISIBLE
        binding.announcementDownloadAttachmentProgress.visibility =
            if (isDownloading) View.VISIBLE else View.INVISIBLE
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
        binding.announcementDownloadAttachmentButton.visibility =
            if (announcement.attachments.isNotEmpty()) View.VISIBLE else View.INVISIBLE
    }

    private fun updateFavorite(isFavorite: Boolean) {
        val resource = if (isFavorite) appR.drawable.ic_star else appR.drawable.ic_star_outline
        binding.announcementFavoriteImageView.setImageResource(resource)
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