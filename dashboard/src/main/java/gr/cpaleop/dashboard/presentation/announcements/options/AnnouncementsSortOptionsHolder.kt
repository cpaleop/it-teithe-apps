package gr.cpaleop.dashboard.presentation.announcements.options

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemOptionSortBinding

class AnnouncementsSortOptionsHolder(
    private val binding: ItemOptionSortBinding,
    private val onClickListener: (Int, Boolean, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AnnouncementSortOption) {
        binding.root.run {
            setOnClickListener { onClickListener(item.type, item.descending, item.selected) }
            isSelected = item.selected
        }
        binding.optionLabel.setText(item.labelResource)
        binding.optionImageView.run {
            visibility = if (item.selected) View.VISIBLE else View.INVISIBLE
            setImageResource(item.imageResource)
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (Int, Boolean, Boolean) -> Unit
        ): AnnouncementsSortOptionsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemOptionSortBinding.inflate(layoutInflater, parent, false)
            return AnnouncementsSortOptionsHolder(binding, onClickListener)
        }
    }
}