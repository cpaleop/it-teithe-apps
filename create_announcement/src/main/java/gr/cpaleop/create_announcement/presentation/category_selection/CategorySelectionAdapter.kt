package gr.cpaleop.create_announcement.presentation.category_selection

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.core.domain.entities.Category

class CategorySelectionAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<Category, CategorySelectionHolder>
        (DIFF_UTIL_CATEGORY) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorySelectionHolder {
        return CategorySelectionHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: CategorySelectionHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_CATEGORY = object : DiffUtil.ItemCallback<Category>() {

            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }
}