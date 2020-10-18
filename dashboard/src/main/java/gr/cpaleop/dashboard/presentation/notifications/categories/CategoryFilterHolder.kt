package gr.cpaleop.dashboard.presentation.notifications.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.core.domain.entities.Category
import gr.cpaleop.dashboard.databinding.ItemCategoryFilterBinding

class CategoryFilterHolder(
    private val binding: ItemCategoryFilterBinding,
    private val onClickListener: (String, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category) {
        binding.categoryFilterName.run {
            /*binding.categoryFilterName.isCloseIconVisible = item.isRegistered*/
            binding.categoryFilterName.isSelected = item.isRegistered
            binding.categoryFilterName.text = item.name
            setOnClickListener {
                onClickListener(item.id, !item.isRegistered)
            }
            /*setOnCloseIconClickListener {
                onClickListener(item.id, false)
            }*/
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (String, Boolean) -> Unit
        ): CategoryFilterHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCategoryFilterBinding.inflate(layoutInflater, parent, false)
            return CategoryFilterHolder(binding, onClickListener)
        }
    }
}