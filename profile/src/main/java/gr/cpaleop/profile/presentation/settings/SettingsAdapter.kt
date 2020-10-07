package gr.cpaleop.profile.presentation.settings

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class SettingsAdapter(private val onClickListener: (Int) -> Unit) :
    ListAdapter<Setting, RecyclerView.ViewHolder>(DIFF_UTIL_SETTING) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TITLE -> SettingsTitleHolder.create(parent)
            VIEW_TYPE_CONTENT -> SettingsHolder.create(parent, onClickListener)
            else -> throw IllegalArgumentException("No type found with value $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_TITLE -> (holder as SettingsTitleHolder).bind(currentList[position])
            VIEW_TYPE_CONTENT -> (holder as SettingsHolder).bind(currentList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position].type) {
            SettingType.SECTION_TITLE -> VIEW_TYPE_TITLE
            SettingType.CONTENT -> VIEW_TYPE_CONTENT
        }
    }

    companion object {

        private const val VIEW_TYPE_TITLE = 0
        private const val VIEW_TYPE_CONTENT = 1
        private val DIFF_UTIL_SETTING = object : DiffUtil.ItemCallback<Setting>() {

            override fun areItemsTheSame(oldItem: Setting, newItem: Setting): Boolean {
                return oldItem.titleRes == newItem.titleRes
            }

            override fun areContentsTheSame(oldItem: Setting, newItem: Setting): Boolean {
                return oldItem == newItem
            }
        }
    }
}