package gr.cpaleop.favorites.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.core.presentation.AnnouncementPresentation

class FavoriteAnnouncementsAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<AnnouncementPresentation, FavoriteAnnouncementsHolder>(
        DIFF_UTIL_FAVORITE_ANNOUNCEMENT
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAnnouncementsHolder {
        return FavoriteAnnouncementsHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: FavoriteAnnouncementsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_FAVORITE_ANNOUNCEMENT =
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