package gr.cpaleop.dashboard.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemProfileDetailBinding

class ProfileHolder(private val binding: ItemProfileDetailBinding) :
    RecyclerView.ViewHolder(binding.root), ProfileBindableHolder {

    override fun bind(item: ProfilePresentationDetails) {
        binding.profileDetailTitle.text = item.title
        binding.profileDetailContent.text = item.content
    }

    companion object {

        fun create(parent: ViewGroup): ProfileHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProfileDetailBinding.inflate(inflater, parent, false)
            return ProfileHolder(binding)
        }
    }
}