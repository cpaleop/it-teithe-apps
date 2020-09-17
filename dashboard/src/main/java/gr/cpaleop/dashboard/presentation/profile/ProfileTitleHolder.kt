package gr.cpaleop.dashboard.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.databinding.ItemProfileTitleHolderBinding
import java.util.*

class ProfileTitleHolder(private val binding: ItemProfileTitleHolderBinding) :
    RecyclerView.ViewHolder(binding.root), ProfileBindableHolder {

    override fun bind(item: ProfileSocialDetails) {
        binding.profileDetailTitle.text = item.label.toUpperCase(Locale.getDefault())
    }

    companion object {

        fun create(parent: ViewGroup): ProfileTitleHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProfileTitleHolderBinding.inflate(inflater, parent, false)
            return ProfileTitleHolder(binding)
        }
    }
}