package gr.cpaleop.categoryfilter.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.categoryfilter.databinding.ItemAnnouncementCategoryBinding
import gr.cpaleop.categoryfilter.domain.entities.Announcement

class AnnouncementHolder(private val binding: ItemAnnouncementCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Announcement) {
        binding.announcementTitle.text = item.title
        binding.announcementContent.text = item.text
        binding.announcementDate.text = item.date
        binding.announcementPublisher.text = item.publisherName
    }

    companion object {

        fun create(parent: ViewGroup): AnnouncementHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAnnouncementCategoryBinding.inflate(layoutInflater, parent, false)
            return AnnouncementHolder(binding)
        }
    }
}