package gr.cpaleop.dashboard.presentation.documents.sort

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class DocumentSortOptionsAdapter(private val onClickListener: (Int, Boolean, Boolean) -> Unit) :
    ListAdapter<DocumentSortOption, DocumentSortOptionsHolder>(DIFF_UTIL_FILE_SORT_OPTION) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentSortOptionsHolder {
        return DocumentSortOptionsHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: DocumentSortOptionsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val DIFF_UTIL_FILE_SORT_OPTION =
            object : DiffUtil.ItemCallback<DocumentSortOption>() {

                override fun areItemsTheSame(
                    oldItem: DocumentSortOption,
                    newItem: DocumentSortOption
                ): Boolean {
                    return oldItem.type == newItem.type
                }

                override fun areContentsTheSame(
                    oldItem: DocumentSortOption,
                    newItem: DocumentSortOption
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}