package gr.cpaleop.profile.presentation.options

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ProfileOptionsAdapter(private val clickListener: (Int) -> Unit) :
    ListAdapter<ProfileOption, ProfileOptionHolder>(DIFF_UTIL_PROFILE_OPTION) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileOptionHolder {
        return ProfileOptionHolder.create(parent, clickListener)
    }

    override fun onBindViewHolder(holder: ProfileOptionHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_PROFILE_OPTION = object : DiffUtil.ItemCallback<ProfileOption>() {

            override fun areItemsTheSame(oldItem: ProfileOption, newItem: ProfileOption): Boolean {
                return oldItem.labelRes == newItem.labelRes
            }

            override fun areContentsTheSame(
                oldItem: ProfileOption,
                newItem: ProfileOption
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}