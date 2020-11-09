package gr.cpaleop.create_announcement.presentation.attachments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.create_announcement.databinding.ItemAttachmentBinding

class AttachmentPresentationHolder(
    private val binding: ItemAttachmentBinding,
    private val onRemoveClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AttachmentPresentation) {
        binding.attachmentNameTextView.futureText = item.name
        binding.attachmentTypeImageView.setImageResource(item.typeDrawableRes)
        binding.attachmentRemoveImageView.setOnClickListener { onRemoveClickListener(item.uri) }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onRemoveClickListener: (String) -> Unit
        ): AttachmentPresentationHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAttachmentBinding.inflate(layoutInflater, parent, false)
            return AttachmentPresentationHolder(binding, onRemoveClickListener)
        }
    }
}