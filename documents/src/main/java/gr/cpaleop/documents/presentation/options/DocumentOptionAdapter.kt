package gr.cpaleop.documents.presentation.options

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gr.cpaleop.documents.domain.entities.DocumentOptionType

class DocumentOptionAdapter(private val onClickListener: (DocumentOptionType) -> Unit) :
    ListAdapter<DocumentOption, DocumentOptionHolder>(FILE_OPTION_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentOptionHolder {
        return DocumentOptionHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: DocumentOptionHolder, position: Int) {
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