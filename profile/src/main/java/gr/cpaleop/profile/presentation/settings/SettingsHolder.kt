package gr.cpaleop.profile.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.profile.databinding.ItemSettingBinding

class SettingsHolder(
    private val binding: ItemSettingBinding,
    private val onClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Setting) {
        binding.run {
            root.setOnClickListener { onClickListener(item.title) }
            item.iconRes?.let(settingIconImageView::setImageResource)
            settingTitleTextView.text = item.title
            settingValueTextView.run {
                isVisible = item.value != null
                text = item.value
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (String) -> Unit): SettingsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSettingBinding.inflate(layoutInflater, parent, false)
            return SettingsHolder(binding, onClickListener)
        }
    }
}