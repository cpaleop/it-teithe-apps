package gr.cpaleop.dashboard.presentation.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ProfileAdapter(private val moreClickListener: (String) -> Unit) :
    ListAdapter<ProfileSocialDetails, ProfileSocialHolder>(PROFILE_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileSocialHolder {
        return ProfileSocialHolder.create(parent, moreClickListener)
    }

    override fun onBindViewHolder(holder: ProfileSocialHolder, position: Int) {
        (holder as ProfileBindableHolder).bind(currentList[position])
    }

    companion object {

        private val PROFILE_DIFF_UTIL =
            object : DiffUtil.ItemCallback<ProfileSocialDetails>() {

                override fun areItemsTheSame(
                    oldItem: ProfileSocialDetails,
                    newItem: ProfileSocialDetails
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: ProfileSocialDetails,
                    newItem: ProfileSocialDetails
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}