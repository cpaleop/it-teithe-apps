package gr.cpaleop.dashboard.presentation.documents.options

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.dashboard.domain.entities.DocumentOptionType

class FileOptionAdapter(private val onClickListener: (DocumentOptionType) -> Unit) :
    ListAdapter<DocumentOption, FileOptionHolder>(FILE_OPTION_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileOptionHolder {
        return FileOptionHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: FileOptionHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val FILE_OPTION_DIFF_UTIL = object : DiffUtil.ItemCallback<DocumentOption>() {

            override fun areItemsTheSame(
                oldItem: DocumentOption,
                newItem: DocumentOption
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DocumentOption,
                newItem: DocumentOption
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}