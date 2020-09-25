package gr.cpaleop.dashboard.presentation.documents.announcement_folder

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder

class AnnouncementFolderAdapter :
    ListAdapter<AnnouncementFolder, AnnouncementFolderHolder>(
        DIFF_UTIL_ANNOUNCEMENT_FOLDER
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementFolderHolder {
        return AnnouncementFolderHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AnnouncementFolderHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_ANNOUNCEMENT_FOLDER =
            object : DiffUtil.ItemCallback<AnnouncementFolder>() {
                override fun areItemsTheSame(
                    oldItem: AnnouncementFolder,
                    newItem: AnnouncementFolder
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: AnnouncementFolder,
                    newItem: AnnouncementFolder
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}