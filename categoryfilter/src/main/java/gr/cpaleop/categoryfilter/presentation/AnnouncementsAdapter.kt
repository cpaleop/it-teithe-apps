package gr.cpaleop.categoryfilter.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.categoryfilter.domain.entities.Announcement

class AnnouncementsAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<Announcement, AnnouncementHolder>(ANNOUNCEMENTS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementHolder {
        return AnnouncementHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: AnnouncementHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val ANNOUNCEMENTS_DIFF_UTIL = object : DiffUtil.ItemCallback<Announcement>() {

            override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement): Boolean {
                return oldItem == newItem
            }
        }
    }
}