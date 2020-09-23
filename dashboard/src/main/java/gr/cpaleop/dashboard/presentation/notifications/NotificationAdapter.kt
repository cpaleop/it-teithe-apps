package gr.cpaleop.dashboard.presentation.notifications

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.dashboard.domain.entities.Notification

class NotificationAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<Notification, NotificationHolder>(NOTIFICATION_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val NOTIFICATION_DIFF_UTIL =
            object : DiffUtil.ItemCallback<Notification>() {

                override fun areItemsTheSame(
                    oldItem: Notification,
                    newItem: Notification
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Notification,
                    newItem: Notification
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}