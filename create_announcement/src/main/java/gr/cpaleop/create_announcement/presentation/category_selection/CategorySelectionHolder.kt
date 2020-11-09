package gr.cpaleop.create_announcement.presentation.category_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.create_announcement.databinding.ItemCategoryBinding

class CategorySelectionHolder(
    private val binding: ItemCategoryBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category) {
        binding.root.setOnClickListener { onClickListener(item.id) }
        binding.categorySelectionLabel.text = item.name
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): CategorySelectionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
            return CategorySelectionHolder(binding, onClickListener)
        }
    }
}