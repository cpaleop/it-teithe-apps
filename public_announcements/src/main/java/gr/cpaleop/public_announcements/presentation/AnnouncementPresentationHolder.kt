package gr.cpaleop.public_announcements.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.public_announcements.databinding.ItemPublicAnnouncementBinding

class AnnouncementPresentationHolder(
    private val binding: ItemPublicAnnouncementBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementPresentation) {
        binding.run {
            root.setOnClickListener { onClickListener(item.id) }
            publicAnnouncementTitle.text = item.title
            publicAnnouncementContent.text = item.content
            publicAnnouncementCategory.text = item.category
            publicAnnouncementDate.text = item.date
            publicAnnouncementPublisher.text = item.publisherName
            publicAnnouncementHasAttachments.isVisible = item.hasAttachments
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (String) -> Unit
        ): AnnouncementPresentationHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPublicAnnouncementBinding.inflate(layoutInflater, parent, false)
            return AnnouncementPresentationHolder(binding, onClickListener)
        }
    }
}