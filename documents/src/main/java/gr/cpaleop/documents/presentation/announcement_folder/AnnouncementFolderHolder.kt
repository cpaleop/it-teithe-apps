package gr.cpaleop.documents.presentation.announcement_folder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.documents.databinding.ItemAnnouncementFolderBinding
import gr.cpaleop.documents.domain.entities.AnnouncementFolder

class AnnouncementFolderHolder(
    private val binding: ItemAnnouncementFolderBinding,
    private val onClickListener: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementFolder) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        binding.announcementFolderTitleText.text = item.title
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): AnnouncementFolderHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAnnouncementFolderBinding.inflate(layoutInflater, parent, false)
            return AnnouncementFolderHolder(binding, onClickListener)
        }
    }
}