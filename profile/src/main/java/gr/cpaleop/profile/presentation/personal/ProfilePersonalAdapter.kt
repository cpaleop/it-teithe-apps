package gr.cpaleop.profile.presentation.personal

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.profile.presentation.ProfilePersonalDetails

class ProfilePersonalAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<ProfilePersonalDetails, ProfilePersonalHolder>(DIFF_UTIL_PERSONAL_DETAILS) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePersonalHolder {
        return ProfilePersonalHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: ProfilePersonalHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_PERSONAL_DETAILS =
            object : DiffUtil.ItemCallback<ProfilePersonalDetails>() {

                override fun areItemsTheSame(
                    oldItem: ProfilePersonalDetails,
                    newItem: ProfilePersonalDetails
                ): Boolean {
                    return oldItem.label == newItem.label
                }

                override fun areContentsTheSame(
                    oldItem: ProfilePersonalDetails,
                    newItem: ProfilePersonalDetails
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}