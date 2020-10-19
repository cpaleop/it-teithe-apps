package gr.cpaleop.dashboard.presentation.notifications

import android.os.Bundle
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

    override fun onBindViewHolder(
        holder: NotificationHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            payloads.forEach { bundle ->
                bundle as Bundle
                if (!bundle.getString(PAYLOAD_TITLE).isNullOrEmpty()) {
                    holder.bindTitle(currentList[position].title)
                } else {
                    super.onBindViewHolder(holder, position, payloads)
                }

                if (!bundle.getString(PAYLOAD_PUBLISHER_NAME).isNullOrEmpty()) {
                    holder.bindPublisherName(currentList[position].publisherName)
                } else {
                    super.onBindViewHolder(holder, position, payloads)
                }
            }
        }
    }

    companion object {

        private const val PAYLOAD_TITLE = "PAYLOAD_TITLE"
        private const val PAYLOAD_PUBLISHER_NAME = "PAYLOAD_PUBLISHER_NAME"

        private val NOTIFICATION_DIFF_UTIL =
            object : DiffUtil.ItemCallback<NotificationPresentation>() {

                override fun areItemsTheSame(
                    oldItem: NotificationPresentation,
                    newItem: NotificationPresentation
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: NotificationPresentation,
                    newItem: NotificationPresentation
                ): Boolean {
                    return oldItem == newItem
                }

                override fun getChangePayload(
                    oldItem: NotificationPresentation,
                    newItem: NotificationPresentation
                ): Any {
                    return Bundle().apply {
                        if (oldItem.title != newItem.title) {
                            putString(
                                PAYLOAD_TITLE,
                                PAYLOAD_TITLE
                            )
                        }
                        if (oldItem.publisherName != newItem.publisherName) {
                            putString(
                                PAYLOAD_PUBLISHER_NAME,
                                PAYLOAD_PUBLISHER_NAME
                            )
                        }
                    }
                }
            }
    }
}