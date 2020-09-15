package gr.cpaleop.announcement.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        viewModel.presentAnnouncement(announcementId)
    }

    override fun onDestroy() {
        unloadKoinModules(announcementModule)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.downloadAttachments()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(appR.anim.fade_in, appR.anim.fade_out)
    }

    private fun handleIntent() {
        announcementId = intent.getStringExtra(ANNOUNCEMENT_ID) ?: ""
    }

    private fun setupViews() {
        binding.announcementBackButton.setOnClickListener {
            onBackPressed()
        }

        binding.announcementDownloadAttachmentButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.downloadAttachments()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.announcement.observe(this, Observer(::showAnnouncement))
        viewModel.attachmentFileId.observe(this, Observer(::initiateDownload))
    }

    private fun showAnnouncement(announcement: AnnouncementDetails) {
        binding.announcementTitle.text = announcement.title
        binding.announcementDate.text = announcement.date
        binding.announcementPublisher.text = announcement.publisherName
        binding.announcementCategory.text = announcement.category
        binding.announcementContent.text = announcement.text
        binding.announcementDownloadAttachmentButton.isVisible =
            announcement.attachments.isNotEmpty()
    }

    private fun initiateDownload(files: Array<String>) {
        DownloadFileWorker.enqueue(applicationContext, files)
    }

    companion object {

        private const val ANNOUNCEMENT_ID = "announcementId"
        private const val PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1227
    }
}