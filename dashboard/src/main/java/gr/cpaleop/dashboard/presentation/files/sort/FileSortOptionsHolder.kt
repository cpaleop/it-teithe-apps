package gr.cpaleop.dashboard.presentation.files.sort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemOptionSortBinding

class FileSortOptionsHolder(
    private val binding: ItemOptionSortBinding,
    private val onClickListener: (Int, Boolean, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DocumentSortOption) {
        binding.root.run {
            setOnClickListener { onClickListener(item.type, item.descending, item.selected) }
            isSelected = item.selected
        }
        binding.optionLabel.setText(item.label)
        binding.optionImageView.run {
            visibility = if (item.selected) View.VISIBLE else View.INVISIBLE
            rotationX = if (!item.descending) 180f else 0f
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (Int, Boolean, Boolean) -> Unit
        ): FileSortOptionsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOptionSortBinding.inflate(layoutInflater, parent, false)
            return FileSortOptionsHolder(binding, onClickListener)
        }
    }
}