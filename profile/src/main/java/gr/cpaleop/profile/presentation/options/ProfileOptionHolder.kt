package gr.cpaleop.profile.presentation.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.profile.databinding.ItemProfileOptionBinding

class ProfileOptionHolder(
    private val binding: ItemProfileOptionBinding,
    private val clickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileOption) {
        binding.root.setOnClickListener { clickListener(item.labelRes) }
        binding.profileOptionImageView.setImageResource(item.iconResource)
        binding.profileOptionLabel.futureText = binding.root.context.getString(item.labelRes)
    }

    companion object {

        fun create(parent: ViewGroup, clickListener: (Int) -> Unit): ProfileOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProfileOptionBinding.inflate(layoutInflater, parent, false)
            return ProfileOptionHolder(binding, clickListener)
        }
    }
}