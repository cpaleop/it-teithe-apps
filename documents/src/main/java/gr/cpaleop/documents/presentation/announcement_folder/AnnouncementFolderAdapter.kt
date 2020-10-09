package gr.cpaleop.documents.presentation.announcement_folder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.documents.domain.entities.AnnouncementFolder

class AnnouncementFolderAdapter(private val onClickListener: (View, String) -> Unit) :
    ListAdapter<AnnouncementFolder, AnnouncementFolderHolder>(
        DIFF_UTIL_ANNOUNCEMENT_FOLDER
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementFolderHolder {
        return AnnouncementFolderHolder.create(parent, onClickListener)
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