package gr.cpaleop.dashboard.presentation.files.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemFileOptionBinding

class FileOptionHolder(
    private val binding: ItemFileOptionBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FileOption) {
        binding.root.setOnClickListener { onClickListener(item.name) }
        binding.fileOptionImageView.setImageResource(item.imageResource)
        binding.fileOptionLabel.text = item.name
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): FileOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemFileOptionBinding.inflate(layoutInflater, parent, false)
            return FileOptionHolder(binding, onClickListener)
        }
    }
}