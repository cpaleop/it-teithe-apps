package gr.cpaleop.dashboard.presentation.files

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemDocumentBinding

class FilesHolder(
    private val binding: ItemDocumentBinding,
    private val onClickListener: (String) -> Unit,
    private val moreClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FileDocument) {
        binding.root.setOnClickListener { onClickListener(item.absolutePath) }
        binding.documentMoreImageView.setOnClickListener { moreClickListener(item.name) }
        binding.documentTitleTextView.text = item.name
        binding.documentLastModifiedTextView.text = item.lastModifiedDate
        binding.documentPreview.setImageResource(item.previewDrawable)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (String) -> Unit,
            moreClickListener: (String) -> Unit
        ): FilesHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDocumentBinding.inflate(layoutInflater, parent, false)
            return FilesHolder(binding, onClickListener, moreClickListener)
        }
    }
}