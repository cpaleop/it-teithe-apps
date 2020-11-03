package gr.cpaleop.favorites.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
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
            favoriteAnnouncementTitle.text = item.title
            favoriteAnnouncementCategory.text = item.category
            favoriteAnnouncementContent.text = item.content
            favoriteAnnouncementPublisher.text = item.publisherName
            favoriteAnnouncementDate.text = item.date
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