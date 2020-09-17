package gr.cpaleop.dashboard.presentation.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProfileAdapter(private val moreClickListener: (String) -> Unit) :
    ListAdapter<ProfileSocialDetails, RecyclerView.ViewHolder>(PROFILE_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CONTENT -> ProfileSocialHolder.create(parent, moreClickListener)
            VIEW_TYPE_TITLE -> ProfileTitleHolder.create(parent)
            else -> throw IllegalArgumentException("View type not found")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ProfileBindableHolder).bind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].type) {
            ProfileDetailsType.CONTENT -> VIEW_TYPE_CONTENT
            ProfileDetailsType.TITLE -> VIEW_TYPE_TITLE
        }
    }

    companion object {

        private const val VIEW_TYPE_CONTENT = 1
        private const val VIEW_TYPE_TITLE = 2

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