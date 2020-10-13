package gr.cpaleop.profile.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.profile.databinding.ItemSettingBinding

class SettingsHolder(
    private val binding: ItemSettingBinding,
    private val onClickListener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Setting) {
        binding.run {
            root.setOnClickListener { onClickListener(item.titleRes) }
            item.iconRes?.let(settingIconImageView::setImageResource)
            settingTitleTextView.setText(item.titleRes)
            settingValueTextView.run {
                isVisible = item.valueRes != null || item.argument != null

                if (item.valueRes != null) {
                    this.text = binding.root.context.getString(
                        item.valueRes,
                        item.argument
                    )
                } else {
                    this.text = ""
                }
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup, onClickListener: (Int) -> Unit): SettingsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemSettingBinding.inflate(layoutInflater, parent, false)
            return SettingsHolder(binding, onClickListener)
        }
    }
}