package gr.cpaleop.profile.presentation.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.profile.databinding.ItemProfileOptionBinding

class ProfileOptionHolder(
    private val binding: ItemProfileOptionBinding,
    private val clickListener: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileOption) {
        binding.root.setOnClickListener { clickListener(item.name) }
        binding.profileOptionImageView.setImageResource(item.iconResource)
        binding.profileOptionLabel.text = item.name
    }

    companion object {

        fun create(parent: ViewGroup, clickListener: (String) -> Unit): ProfileOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProfileOptionBinding.inflate(layoutInflater, parent, false)
            return ProfileOptionHolder(binding, clickListener)
        }
    }
}