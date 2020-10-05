package gr.cpaleop.profile.presentation.socials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.profile.databinding.ItemProfileSocialBinding
import gr.cpaleop.profile.presentation.ProfileSocialDetails

class ProfileSocialHolder(
    private val binding: ItemProfileSocialBinding,
    private val moreClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileSocialDetails) {
        binding.profileSocialMoreImageView.setOnClickListener { moreClickListener(item.label) }
        binding.profileSocialLogoImageView.setImageResource(item.socialLogoResource)
        binding.profileSocialContentTextView.text = item.value
    }

    fun bindContent(item: String) {
        binding.profileSocialContentTextView.text = item
    }

    companion object {

        fun create(parent: ViewGroup, moreClickListener: (String) -> Unit): ProfileSocialHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProfileSocialBinding.inflate(inflater, parent, false)
            return ProfileSocialHolder(binding, moreClickListener)
        }
    }
}