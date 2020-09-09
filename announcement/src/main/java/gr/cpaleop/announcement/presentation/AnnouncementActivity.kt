package gr.cpaleop.announcement.presentation

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import gr.cpaleop.announcement.databinding.ActivityAnnouncementBinding
import gr.cpaleop.announcement.di.announcementModule
import gr.cpaleop.core.presentation.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

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

    private fun handleIntent() {
        announcementId = intent.getStringExtra(ANNOUNCEMENT_ID) ?: ""
    }

    private fun setupViews() {
        binding.announcementBackButton.setOnClickListener {
            onBackPressed()
        }

        binding.announcementDownloadAttachmentButton.setOnClickListener {
            viewModel.downloadAttachments()
        }
    }

    private fun observeViewModel() {
        viewModel.announcement.observe(this, Observer(::showAnnouncement))
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

    companion object {

        private const val ANNOUNCEMENT_ID = "announcementId"
    }
}