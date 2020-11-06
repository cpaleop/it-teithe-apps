package gr.cpaleop.dashboard.presentation.notifications

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.dashboard.databinding.ItemNotificationBinding

class NotificationHolder(
    private val binding: ItemNotificationBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationPresentation) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        bindTitle(item.title)
        binding.announcementDate.futureText = item.date
        binding.announcementCategory.futureText = item.category
        bindPublisherName(item.publisherName)
    }

    fun bindTitle(title: SpannableString) {
        binding.announcementTitle.futureText = title
    }

    fun bindPublisherName(publisherName: SpannableString) {
        binding.announcementPublisher.futureText = publisherName
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): NotificationHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemNotificationBinding.inflate(inflater, parent, false)
            return NotificationHolder(binding, onClickListener)
        }
    }
}