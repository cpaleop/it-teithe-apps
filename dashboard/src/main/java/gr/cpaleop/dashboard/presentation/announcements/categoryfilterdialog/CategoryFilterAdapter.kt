package gr.cpaleop.dashboard.presentation.announcements.categoryfilterdialog

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class CategoryFilterAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<CategoryFilter, CategoryFilterHolder>(CATEGORY_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFilterHolder {
        return CategoryFilterHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: CategoryFilterHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val CATEGORY_DIFF_UTIL = object : DiffUtil.ItemCallback<CategoryFilter>() {

            override fun areItemsTheSame(
                oldItem: CategoryFilter,
                newItem: CategoryFilter
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CategoryFilter,
                newItem: CategoryFilter
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}