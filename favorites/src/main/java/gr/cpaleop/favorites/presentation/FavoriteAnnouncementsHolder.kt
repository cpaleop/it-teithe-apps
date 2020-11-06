package gr.cpaleop.favorites.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.core.presentation.AnnouncementPresentation
import gr.cpaleop.favorites.databinding.ItemFavoriteAnnouncementBinding

class FavoriteAnnouncementsHolder(
    private val binding: ItemFavoriteAnnouncementBinding,
    private val onClickListener: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementPresentation) {
        binding.run {
            root.setOnClickListener { onClickListener(item.id) }
            favoriteAnnouncementTitle.futureText = item.title
            favoriteAnnouncementCategory.futureText = item.category
            favoriteAnnouncementContent.futureText = item.content
            favoriteAnnouncementPublisher.futureText = item.publisherName
            favoriteAnnouncementDate.futureText = item.date
            favoriteAnnouncementHasAttachments.isVisible = item.hasAttachments
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (String) -> Unit
        ): FavoriteAnnouncementsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemFavoriteAnnouncementBinding.inflate(layoutInflater, parent, false)
            return FavoriteAnnouncementsHolder(binding, onClickListener)
        }
    }
}