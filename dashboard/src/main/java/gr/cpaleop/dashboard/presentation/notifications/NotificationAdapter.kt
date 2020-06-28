package gr.cpaleop.dashboard.presentation.notifications

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class NotificationAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<NotificationPresentation, NotificationHolder>(NOTIFICATION_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val NOTIFICATION_DIFF_UTIL =
            object : DiffUtil.ItemCallback<NotificationPresentation>() {

                override fun areItemsTheSame(
                    oldItem: NotificationPresentation,
                    newItem: NotificationPresentation
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: NotificationPresentation,
                    newItem: NotificationPresentation
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }
}