package gr.cpaleop.dashboard.presentation.announcements.options

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemOptionSortBinding
import gr.cpaleop.dashboard.presentation.announcements.options.sort.SortOption

class SortOptionsHolder(private val binding: ItemOptionSortBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SortOption) {
        binding.optionLabel.setText(item.nameResource)
        binding.optionImageView.visibility = if (item.selected) View.VISIBLE else View.INVISIBLE
        binding.optionImageView.setImageResource(item.type.imageRes)
    }

    companion object {

        fun create(parent: ViewGroup): SortOptionsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOptionSortBinding.inflate(layoutInflater, parent, false)
            return SortOptionsHolder(binding)
        }
    }
}