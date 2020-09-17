package gr.cpaleop.dashboard.presentation.profile.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemFileOptionBinding

// Use existing layout, doesn't matter
class ProfileOptionHolder(
    private val binding: ItemFileOptionBinding,
    private val clickListener: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileOption) {
        binding.root.setOnClickListener { clickListener(item.name) }
        binding.fileOptionImageView.setImageResource(item.iconResource)
        binding.fileOptionLabel.text = item.name
    }

    companion object {

        fun create(parent: ViewGroup, clickListener: (String) -> Unit): ProfileOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemFileOptionBinding.inflate(layoutInflater, parent, false)
            return ProfileOptionHolder(binding, clickListener)
        }
    }
}