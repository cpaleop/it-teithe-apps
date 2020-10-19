package gr.cpaleop.documents.presentation.announcement_folder

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.documents.databinding.ItemAnnouncementFolderBinding

class AnnouncementFolderHolder(
    private val binding: ItemAnnouncementFolderBinding,
    private val onClickListener: (View, String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementFolderPresentation) {
        binding.root.run {
            setOnClickListener { onClickListener(it, item.id) }
        }
        binding.announcementFolderTitleText.text = item.title
    }

    fun bindTitle(title: SpannableString) {
        binding.announcementFolderTitleText.text = title
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (View, String) -> Unit
        ): AnnouncementFolderHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAnnouncementFolderBinding.inflate(layoutInflater, parent, false)
            return AnnouncementFolderHolder(binding, onClickListener)
        }
    }
}