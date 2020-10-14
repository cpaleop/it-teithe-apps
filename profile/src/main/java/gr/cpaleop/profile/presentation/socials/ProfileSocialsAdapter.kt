package gr.cpaleop.profile.presentation.socials

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.profile.domain.entities.Social
import gr.cpaleop.profile.presentation.ProfileSocialDetails

class ProfileSocialsAdapter(
    private val clickListener: (String) -> Unit,
    private val moreClickListener: (String, Social) -> Unit
) :
    ListAdapter<ProfileSocialDetails, ProfileSocialHolder>(PROFILE_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileSocialHolder {
        return ProfileSocialHolder.create(parent, clickListener, moreClickListener)
    }

    override fun onBindViewHolder(holder: ProfileSocialHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(
        holder: ProfileSocialHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else {
            if (payloads.contains(PAYLOAD_CONTENT)) {
                holder.bindContent(currentList[position].value)
            }
        }
    }

    companion object {

        private const val PAYLOAD_CONTENT = "PAYLOAD_CONTENT"

        private val PROFILE_DIFF_UTIL =
            object : DiffUtil.ItemCallback<ProfileSocialDetails>() {

                override fun areItemsTheSame(
                    oldItem: ProfileSocialDetails,
                    newItem: ProfileSocialDetails
                ): Boolean {
                    return oldItem.socialType == newItem.socialType
                }

                override fun areContentsTheSame(
                    oldItem: ProfileSocialDetails,
                    newItem: ProfileSocialDetails
                ): Boolean {
                    return oldItem == newItem
                }

                override fun getChangePayload(
                    oldItem: ProfileSocialDetails,
                    newItem: ProfileSocialDetails
                ): Any? {
                    if (oldItem.value != newItem.value) return PAYLOAD_CONTENT
                    return super.getChangePayload(oldItem, newItem)
                }
            }
    }
}