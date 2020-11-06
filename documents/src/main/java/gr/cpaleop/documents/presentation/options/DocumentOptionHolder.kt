package gr.cpaleop.documents.presentation.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.documents.databinding.ItemDocumentOptionBinding
import gr.cpaleop.documents.domain.entities.DocumentOptionType

class DocumentOptionHolder(
    private val binding: ItemDocumentOptionBinding,
    private val onClickListener: (DocumentOptionType) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DocumentOption) {
        binding.root.setOnClickListener { onClickListener(item.type) }
        binding.documentOptionImageView.setImageResource(item.iconResource)
        binding.documentOptionLabel.futureText = binding.root.context.getString(item.name)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (DocumentOptionType) -> Unit
        ): DocumentOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDocumentOptionBinding.inflate(layoutInflater, parent, false)
            return DocumentOptionHolder(binding, onClickListener)
        }
    }
}