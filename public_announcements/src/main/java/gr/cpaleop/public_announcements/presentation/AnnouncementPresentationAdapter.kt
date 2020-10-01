package gr.cpaleop.public_announcements.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.core.presentation.AnnouncementPresentation

class AnnouncementPresentationAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<AnnouncementPresentation, AnnouncementPresentationHolder>(
        DIFF_UTIL_ANNOUNCEMENT_PRESENTATION
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementPresentationHolder {
        return AnnouncementPresentationHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: AnnouncementPresentationHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_ANNOUNCEMENT_PRESENTATION =
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