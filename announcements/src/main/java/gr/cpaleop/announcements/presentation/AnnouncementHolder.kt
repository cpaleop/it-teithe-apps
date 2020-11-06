package gr.cpaleop.announcements.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.announcements.databinding.ItemAnnouncementBinding
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.core.presentation.AnnouncementPresentation

class AnnouncementHolder(
    private val binding: ItemAnnouncementBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementPresentation) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        binding.announcementTitle.futureText = item.title
        binding.announcementDate.futureText = item.date
        binding.announcementPublisher.futureText = item.publisherName
        binding.announcementCategory.futureText = item.category
        binding.announcementContent.futureText = item.content
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