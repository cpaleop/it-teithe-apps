package gr.cpaleop.documents.presentation.document

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.documents.databinding.ItemDocumentBinding

class DocumentsHolder(
    private val binding: ItemDocumentBinding,
    private val onClickListener: (String) -> Unit,
    private val moreClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FileDocument) {
        binding.root.setOnClickListener { onClickListener(item.absolutePath) }
        binding.documentMoreImageView.setOnClickListener { moreClickListener(item.uri) }
        binding.documentTitleTextView.text = item.name
        binding.documentLastModifiedTextView.text = binding.root.context.getString(
            item.lastModifiedDate.labelRes,
            item.lastModifiedDate.dateHumanReadable
        )
        binding.documentPreview.setImageResource(item.previewDrawable)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (String) -> Unit,
            moreClickListener: (String) -> Unit
        ): DocumentsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDocumentBinding.inflate(layoutInflater, parent, false)
            return DocumentsHolder(binding, onClickListener, moreClickListener)
        }
    }
}