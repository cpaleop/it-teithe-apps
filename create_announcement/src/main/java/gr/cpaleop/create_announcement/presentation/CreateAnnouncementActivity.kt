package gr.cpaleop.create_announcement.presentation

import gr.cpaleop.create_announcement.databinding.ActivityCreateAnnouncementBinding
import gr.cpaleop.teithe_apps.presentation.base.BaseActivity

class CreateAnnouncementActivity : BaseActivity<ActivityCreateAnnouncementBinding>() {

    override fun inflateViewBinding(): ActivityCreateAnnouncementBinding {
        return ActivityCreateAnnouncementBinding.inflate(layoutInflater)
    }
}