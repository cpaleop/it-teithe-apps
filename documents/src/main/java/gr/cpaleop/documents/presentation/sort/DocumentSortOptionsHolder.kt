package gr.cpaleop.documents.presentation.sort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.documents.databinding.ItemOptionSortBinding

class DocumentSortOptionsHolder(
    private val binding: ItemOptionSortBinding,
    private val onClickListener: (Int, Boolean, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DocumentSortOption) {
        binding.root.run {
            setOnClickListener { onClickListener(item.type, item.descending, item.selected) }
            isSelected = item.selected
        }
        binding.optionLabel.run {
            setText(item.labelResource)
            isSelected = item.selected
        }
        binding.optionImageView.run {
            visibility = if (item.selected) View.VISIBLE else View.INVISIBLE
            setImageResource(item.imageResource)
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (Int, Boolean, Boolean) -> Unit
        ): DocumentSortOptionsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOptionSortBinding.inflate(layoutInflater, parent, false)
            return DocumentSortOptionsHolder(binding, onClickListener)
        }
    }
}