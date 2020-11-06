package gr.cpaleop.profile.presentation.personal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.profile.databinding.ItemProfilePersonalDetailBinding
import gr.cpaleop.profile.domain.entities.Personal
import gr.cpaleop.profile.presentation.ProfilePersonalDetails

class ProfilePersonalHolder(
    private val binding: ItemProfilePersonalDetailBinding,
    private val onClickListener: (String, Personal) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfilePersonalDetails) {
        binding.run {
            profileDetailEditImageView.setOnClickListener {
                onClickListener(
                    binding.root.context.getString(
                        item.label
                    ), item.type
                )
            }
            profileDetailLabelTextView.futureText = root.context.getString(item.label)
            profileDetailValueTextView.futureText = item.value
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onClickListener: (String, Personal) -> Unit
        ): ProfilePersonalHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProfilePersonalDetailBinding.inflate(layoutInflater, parent, false)
            return ProfilePersonalHolder(binding, onClickListener)
        }
    }
}