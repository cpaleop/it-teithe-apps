package gr.cpaleop.dashboard.presentation.documents.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemDocumentOptionBinding
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

class FileOptionHolder(
    private val binding: ItemDocumentOptionBinding,
    private val onClickListener: (DocumentOptionType) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DocumentOption) {
        binding.root.setOnClickListener { onClickListener(item.type) }
        binding.documentOptionImageView.setImageResource(item.iconResource)
        binding.documentOptionLabel.setText(item.name)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (DocumentOptionType) -> Unit
        ): FileOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDocumentOptionBinding.inflate(layoutInflater, parent, false)
            return FileOptionHolder(binding, onClickListener)
        }
    }
}