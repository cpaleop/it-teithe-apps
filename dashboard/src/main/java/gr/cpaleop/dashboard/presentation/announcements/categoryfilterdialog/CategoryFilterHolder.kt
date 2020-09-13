package gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.databinding.ItemCategoryBinding

class CategoryFilterHolder(private val binding: ItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category) {
        binding.categoryFilterLabel.text = item.name
    }

    companion object {

        fun create(parent: ViewGroup): CategoryFilterHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
            return CategoryFilterHolder(binding)
        }
    }
}