package gr.cpaleop.announcements.presentation

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import gr.cpaleop.core.presentation.AnnouncementPresentation

class AnnouncementAdapter(private val onClickListener: (String) -> Unit) :
    PagingDataAdapter<AnnouncementPresentation, AnnouncementHolder>(ANNOUNCEMENT_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementHolder {
        return AnnouncementHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: AnnouncementHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    companion object {

        private val ANNOUNCEMENT_DIFF_UTIL =
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
            }
    }
}