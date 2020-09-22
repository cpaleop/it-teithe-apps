package gr.cpaleop.dashboard.presentation.announcements.options

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class AnnouncementsSortOptionsAdapter(private val onClickListener: (Int, Boolean, Boolean) -> Unit) :
    ListAdapter<AnnouncementSortOption, AnnouncementsSortOptionsHolder>(OPTIONS_DIFF_UTIL) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementsSortOptionsHolder {
        return AnnouncementsSortOptionsHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: AnnouncementsSortOptionsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val OPTIONS_DIFF_UTIL = object : DiffUtil.ItemCallback<AnnouncementSortOption>() {

            override fun areItemsTheSame(
                oldItem: AnnouncementSortOption,
                newItem: AnnouncementSortOption
            ): Boolean {
                return oldItem.labelResource == newItem.labelResource
            }

            override fun areContentsTheSame(
                oldItem: AnnouncementSortOption,
                newItem: AnnouncementSortOption
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}