package gr.cpaleop.dashboard.presentation.announcements.options

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.dashboard.presentation.announcements.options.sort.SortOption

class SortOptionsAdapter : ListAdapter<SortOption, SortOptionsHolder>(OPTIONS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortOptionsHolder {
        return SortOptionsHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SortOptionsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val OPTIONS_DIFF_UTIL = object : DiffUtil.ItemCallback<SortOption>() {

            override fun areItemsTheSame(oldItem: SortOption, newItem: SortOption): Boolean {
                return oldItem.nameResource == newItem.nameResource
            }

            override fun areContentsTheSame(oldItem: SortOption, newItem: SortOption): Boolean {
                return oldItem == newItem
            }
        }
    }
}