package gr.cpaleop.dashboard.presentation.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemNotificationBinding

class NotificationHolder(
    private val binding: ItemNotificationBinding,
    private val onClickListener: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NotificationPresentation) {
        binding.root.setOnClickListener { onClickListener(item.announcement.id) }
        binding.root.setBackgroundResource(item.background)
        binding.notificationTitle.text = item.announcement.title
        binding.notificationDate.text = item.announcement.date
        binding.notificationCategory.text = item.announcement.category
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): NotificationHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemNotificationBinding.inflate(inflater, parent, false)
            return NotificationHolder(binding, onClickListener)
        }
    }
}