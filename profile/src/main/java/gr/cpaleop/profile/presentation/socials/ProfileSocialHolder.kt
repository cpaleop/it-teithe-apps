package gr.cpaleop.profile.presentation.socials

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.profile.databinding.ItemProfileSocialBinding
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.presentation.ProfileSocialDetails

class ProfileSocialHolder(
    private val binding: ItemProfileSocialBinding,
    private val clickListener: (String) -> Unit,
    private val moreClickListener: (String, Social) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileSocialDetails) {
        binding.root.setOnClickListener { clickListener(item.value) }
        binding.profileSocialMoreImageView.setOnClickListener {
            moreClickListener(
                binding.root.context.getString(item.labelRes),
                item.socialType
            )
        }
        binding.profileSocialLogoImageView.setImageResource(item.socialLogoResource)
        bindContent(item.value)
    }

    fun bindContent(item: String) {
        binding.profileSocialContentTextView.futureText = item
    }

    companion object {

        fun create(
            parent: ViewGroup,
            clickListener: (String) -> Unit,
            moreClickListener: (String, Social) -> Unit
        ): ProfileSocialHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProfileSocialBinding.inflate(inflater, parent, false)
            return ProfileSocialHolder(binding, clickListener, moreClickListener)
        }
    }
}