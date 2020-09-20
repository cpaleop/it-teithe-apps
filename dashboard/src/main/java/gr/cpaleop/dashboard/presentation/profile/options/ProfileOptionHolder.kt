package gr.cpaleop.dashboard.presentation.profile.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemDocumentOptionBinding

// Use existing layout, doesn't matter
class ProfileOptionHolder(
    private val binding: ItemDocumentOptionBinding,
    private val clickListener: (String) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileOption) {
        binding.root.setOnClickListener { clickListener(item.name) }
        binding.documentOptionImageView.setImageResource(item.iconResource)
        binding.documentOptionLabel.text = item.name
    }

    companion object {

        fun create(parent: ViewGroup, clickListener: (String) -> Unit): ProfileOptionHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDocumentOptionBinding.inflate(layoutInflater, parent, false)
            return ProfileOptionHolder(binding, clickListener)
        }
    }
}