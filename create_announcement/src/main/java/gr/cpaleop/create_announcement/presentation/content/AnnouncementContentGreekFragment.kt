package gr.cpaleop.create_announcement.presentation.content

import android.view.LayoutInflater
import android.view.ViewGroup
import gr.cpaleop.create_announcement.databinding.FragmentContentBinding
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment

class AnnouncementContentGreekFragment :
    BaseApiFragment<FragmentContentBinding, CreateAnnouncementViewModel>(CreateAnnouncementViewModel::class) {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContentBinding {
        return FragmentContentBinding.inflate(inflater, container, false)
    }
}