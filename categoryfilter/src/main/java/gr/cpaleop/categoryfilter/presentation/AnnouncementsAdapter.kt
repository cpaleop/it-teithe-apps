package gr.cpaleop.categoryfilter.presentation

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.core.presentation.AnnouncementPresentation

class AnnouncementsAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<AnnouncementPresentation, AnnouncementHolder>(ANNOUNCEMENTS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementHolder {
        return AnnouncementHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: AnnouncementHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(
        holder: AnnouncementHolder,
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

                if (!bundle.getString(PAYLOAD_CONTENT).isNullOrEmpty()) {
                    holder.bindContent(currentList[position].content)
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
        private const val PAYLOAD_CONTENT = "PAYLOAD_CONTENT"
        private const val PAYLOAD_PUBLISHER_NAME = "PAYLOAD_PUBLISHER_NAME"

        private val ANNOUNCEMENTS_DIFF_UTIL =
            object : DiffUtil.ItemCallback<AnnouncementPresentation>() {

                override fun areItemsTheSame(
                    oldItem: AnnouncementPresentation,
                    newItem: AnnouncementPresentation
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: AnnouncementPresentation,
                    newItem: AnnouncementPresentation
                ): Boolean {
                    return oldItem == newItem
                }

                override fun getChangePayload(
                    oldItem: AnnouncementPresentation,
                    newItem: AnnouncementPresentation
                ): Any {
                    return Bundle().apply {
                        if (oldItem.title != newItem.title) {
                            putString(
                                PAYLOAD_TITLE,
                                PAYLOAD_TITLE
                            )
                        }
                        if (oldItem.content != newItem.content) {
                            putString(
                                PAYLOAD_CONTENT,
                                PAYLOAD_CONTENT
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