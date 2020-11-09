package gr.cpaleop.create_announcement.presentation

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import gr.cpaleop.create_announcement.databinding.ActivityCreateAnnouncementBinding
import gr.cpaleop.create_announcement.di.createAnnouncementKoinModule
import gr.cpaleop.teithe_apps.presentation.base.BaseApiActivity
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class CreateAnnouncementActivity : BaseApiActivity<ActivityCreateAnnouncementBinding,
        CreateAnnouncementViewModel>(CreateAnnouncementViewModel::class) {

    override fun inflateViewBinding(): ActivityCreateAnnouncementBinding {
        return ActivityCreateAnnouncementBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(createAnnouncementKoinModule)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        unloadKoinModules(createAnnouncementKoinModule)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CODE_FILE_SELECTION -> if (resultCode == RESULT_OK) {
                handleFileSelection(data?.clipData, data?.data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFileSelection(resultClipData: ClipData?, uri: Uri?) {
        if (resultClipData != null) {
            val itemCount = resultClipData.itemCount
            val attachmentUriList = mutableListOf<String>()
            for (i in 0 until itemCount) {
                attachmentUriList.add(resultClipData.getItemAt(i).uri.toString())
            }
            viewModel.addAttachments(attachmentUriList)
        } else {
            val uriList = listOf(uri?.toString() ?: return)
            viewModel.addAttachments(uriList)
        }
    }

    companion object {

        const val CODE_FILE_SELECTION = 0x1227
    }
}