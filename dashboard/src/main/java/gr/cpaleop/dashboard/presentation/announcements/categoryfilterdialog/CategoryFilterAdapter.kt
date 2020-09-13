package gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.core.domain.entities.Category

class CategoryFilterAdapter : ListAdapter<Category, CategoryFilterHolder>(CATEGORY_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFilterHolder {
        return CategoryFilterHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CategoryFilterHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val CATEGORY_DIFF_UTIL = object : DiffUtil.ItemCallback<Category>() {

            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }
}