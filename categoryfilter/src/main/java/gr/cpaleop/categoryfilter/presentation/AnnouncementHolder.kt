package gr.cpaleop.categoryfilter.presentation

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.categoryfilter.databinding.ItemAnnouncementCategoryBinding
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.core.presentation.AnnouncementPresentation

class AnnouncementHolder(
    private val binding: ItemAnnouncementCategoryBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementPresentation) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        binding.announcementTitle.futureText = item.title
        binding.announcementContent.futureText = item.content
        binding.announcementDate.futureText = item.date
        binding.announcementPublisher.futureText = item.publisherName
        binding.announcementCategory.isVisible = item.hasAttachments
    }

    fun bindTitle(title: SpannableString) {
        binding.announcementTitle.futureText = title
    }

    fun bindContent(content: SpannableString) {
        binding.announcementContent.futureText = content
    }

    fun bindPublisherName(publisherName: SpannableString) {
        binding.announcementPublisher.futureText = publisherName
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): AnnouncementHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAnnouncementCategoryBinding.inflate(layoutInflater, parent, false)
            return AnnouncementHolder(binding, onClickListener)
        }
    }
}