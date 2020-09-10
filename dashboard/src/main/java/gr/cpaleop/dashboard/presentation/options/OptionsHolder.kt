package gr.cpaleop.dashboard.presentation.options

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemOptionSortBinding
import gr.cpaleop.dashboard.presentation.options.sort.SortOption

class OptionsHolder(private val binding: ItemOptionSortBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SortOption) {
        binding.optionName.setText(item.nameResource)
        binding.optionImageView.visibility = if (item.selected) View.VISIBLE else View.INVISIBLE
        binding.optionImageView.setImageResource(item.type.imageRes)
    }

    companion object {

        fun create(parent: ViewGroup): OptionsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOptionSortBinding.inflate(layoutInflater, parent, false)
            return OptionsHolder(binding)
        }
    }
}