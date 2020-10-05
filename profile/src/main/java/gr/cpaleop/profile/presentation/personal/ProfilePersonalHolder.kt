package gr.cpaleop.profile.presentation.personal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.profile.databinding.ItemProfilePersonalDetailBinding
import gr.cpaleop.profile.presentation.ProfilePersonalDetails

class ProfilePersonalHolder(
    private val binding: ItemProfilePersonalDetailBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfilePersonalDetails) {
        binding.run {
            profileDetailEditImageView.setOnClickListener { onClickListener(item.label) }
            profileDetailLabelTextView.text = item.label
            profileDetailValueTextView.text = item.value
        }
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): ProfilePersonalHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProfilePersonalDetailBinding.inflate(layoutInflater, parent, false)
            return ProfilePersonalHolder(binding, onClickListener)
        }
    }
}