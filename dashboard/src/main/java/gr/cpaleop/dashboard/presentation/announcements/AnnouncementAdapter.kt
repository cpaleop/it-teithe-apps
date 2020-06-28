package gr.cpaleop.dashboard.presentation.announcements

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

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
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: AnnouncementPresentation,
                    newItem: AnnouncementPresentation
                ): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }
}