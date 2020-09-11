package gr.cpaleop.dashboard.presentation.notifications.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemCategoryFilterBinding
import gr.cpaleop.dashboard.domain.entities.Category

class CategoryFilterHolder(
    private val binding: ItemCategoryFilterBinding,
    private val onClickListener: (String, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category) {
        binding.categoryFilterName.isCloseIconVisible = item.isRegistered
        binding.categoryFilterName.isChecked = item.isRegistered
        binding.categoryFilterName.text = item.name

        binding.categoryFilterName.setOnClickListener {
            onClickListener(item.id, !item.isRegistered)
        }
        binding.categoryFilterName.setOnCloseIconClickListener {
            onClickListener(item.id, false)
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