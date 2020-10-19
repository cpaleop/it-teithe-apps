package gr.cpaleop.documents.presentation.announcement_folder

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class AnnouncementFolderAdapter(private val onClickListener: (View, String) -> Unit) :
    ListAdapter<AnnouncementFolderPresentation, AnnouncementFolderHolder>(
        DIFF_UTIL_ANNOUNCEMENT_FOLDER
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementFolderHolder {
        return AnnouncementFolderHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: AnnouncementFolderHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(
        holder: AnnouncementFolderHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            payloads.forEach {
                it as Bundle
                if (it.getString(PAYLOAD_TITLE).isNullOrEmpty())
                    super.onBindViewHolder(holder, position, payloads)
                else
                    holder.bindTitle(currentList[position].title)
            }
        }
    }

    companion object {

        private const val PAYLOAD_TITLE = "PAYLOAD_TITLE"

        private val DIFF_UTIL_ANNOUNCEMENT_FOLDER =
            object : DiffUtil.ItemCallback<AnnouncementFolderPresentation>() {

                override fun areItemsTheSame(
                    oldItem: AnnouncementFolderPresentation,
                    newItem: AnnouncementFolderPresentation
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: AnnouncementFolderPresentation,
                    newItem: AnnouncementFolderPresentation
                ): Boolean {
                    return oldItem == newItem
                }

                override fun getChangePayload(
                    oldItem: AnnouncementFolderPresentation,
                    newItem: AnnouncementFolderPresentation
                ): Any {
                    return Bundle().apply {
                        if (oldItem.title != newItem.title) putString(PAYLOAD_TITLE, PAYLOAD_TITLE)
                    }
                }
            }
    }
}