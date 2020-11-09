package gr.cpaleop.create_announcement.presentation.attachments

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class AttachmentPresentationAdapter(
    private val onRemoveClickListener: (String) -> Unit
) : ListAdapter<AttachmentPresentation, AttachmentPresentationHolder>(DIFF_UTIL_ATTACHMENT) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AttachmentPresentationHolder {
        return AttachmentPresentationHolder.create(parent, onRemoveClickListener)
    }

    override fun onBindViewHolder(holder: AttachmentPresentationHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_ATTACHMENT =
            object : DiffUtil.ItemCallback<AttachmentPresentation>() {
                override fun areItemsTheSame(
                    oldItem: AttachmentPresentation,
                    newItem: AttachmentPresentation
                ): Boolean {
                    return oldItem.uri == newItem.uri
                }

                override fun areContentsTheSame(
                    oldItem: AttachmentPresentation,
                    newItem: AttachmentPresentation
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}