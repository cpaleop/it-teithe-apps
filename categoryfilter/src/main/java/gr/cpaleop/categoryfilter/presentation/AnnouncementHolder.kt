package gr.cpaleop.categoryfilter.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.categoryfilter.databinding.ItemAnnouncementCategoryBinding
import gr.cpaleop.core.presentation.AnnouncementPresentation

class AnnouncementHolder(
    private val binding: ItemAnnouncementCategoryBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementPresentation) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        binding.announcementTitle.text = item.title
        binding.announcementContent.text = item.content
        binding.announcementDate.text = item.date
        binding.announcementPublisher.text = item.publisherName
        binding.announcementCategory.isVisible = item.hasAttachments
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): AnnouncementHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAnnouncementCategoryBinding.inflate(layoutInflater, parent, false)
            return AnnouncementHolder(binding, onClickListener)
        }
    }
}