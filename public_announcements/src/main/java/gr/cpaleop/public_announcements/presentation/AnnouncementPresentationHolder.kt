package gr.cpaleop.public_announcements.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.public_announcements.databinding.ItemAnnouncementBinding

class AnnouncementPresentationHolder(private val binding: ItemAnnouncementBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementPresentation) {
        binding.run {
            announcementTitle.text = item.title
            announcementContent.text = item.content
            announcementCategory.text = item.category
            announcementDate.text = item.date
            announcementPublisher.text = item.publisherName
            announcementHasAttachments.isVisible = item.hasAttachments
        }
    }

    companion object {

        fun create(parent: ViewGroup): AnnouncementPresentationHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAnnouncementBinding.inflate(layoutInflater, parent, false)
            return AnnouncementPresentationHolder(binding)
        }
    }
}