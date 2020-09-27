package gr.cpaleop.announcements.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.announcements.databinding.ItemAnnouncementBinding

class AnnouncementHolder(
    private val binding: ItemAnnouncementBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: gr.cpaleop.announcements.presentation.AnnouncementPresentation) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        binding.announcementTitle.text = item.title
        binding.announcementDate.text = item.date
        binding.announcementPublisher.text = item.publisherName
        binding.announcementCategory.text = item.category
        binding.announcementContent.text = item.content
        binding.announcementHasAttachments.isVisible = item.hasAttachments
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): AnnouncementHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemAnnouncementBinding.inflate(inflater, parent, false)
            return AnnouncementHolder(binding, onClickListener)
        }
    }
}