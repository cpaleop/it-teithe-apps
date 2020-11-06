package gr.cpaleop.profile.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.common.extensions.futureText
import gr.cpaleop.profile.databinding.ItemSettingsSectionBinding

class SettingsTitleHolder(private val binding: ItemSettingsSectionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Setting) {
        binding.settingsSectionTitleTextView.futureText =
            binding.root.context.getString(item.titleRes)
    }

    companion object {

        fun create(parent: ViewGroup): SettingsTitleHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSettingsSectionBinding.inflate(layoutInflater, parent, false)
            return SettingsTitleHolder(binding)
        }
    }
}