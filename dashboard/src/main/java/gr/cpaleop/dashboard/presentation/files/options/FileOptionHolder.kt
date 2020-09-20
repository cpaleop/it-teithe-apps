package gr.cpaleop.dashboard.presentation.files.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemFileOptionBinding
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

class FileOptionHolder(
    private val binding: ItemFileOptionBinding,
    private val onClickListener: (DocumentOptionType) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DocumentOption) {
        binding.root.setOnClickListener { onClickListener(item.type) }
        binding.fileOptionImageView.setImageResource(item.iconResource)
        binding.fileOptionLabel.setText(item.name)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (DocumentOptionType) -> Unit
        ): FileOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemFileOptionBinding.inflate(layoutInflater, parent, false)
            return FileOptionHolder(binding, onClickListener)
        }
    }
}