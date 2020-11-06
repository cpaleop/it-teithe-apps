package gr.cpaleop.announcements.presentation.categoryfilterdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.announcements.databinding.ItemCategoryBinding
import gr.cpaleop.common.extensions.futureText

class CategoryFilterHolder(
    private val binding: ItemCategoryBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryFilter) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        binding.categoryFilterLabel.run {
            futureText = item.name
            isSelected = item.selected
        }
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): CategoryFilterHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
            return CategoryFilterHolder(binding, onClickListener)
        }
    }
}