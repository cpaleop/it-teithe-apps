package gr.cpaleop.dashboard.presentation.documents.document

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class DocumentsAdapter(
    private val onClickListener: (String) -> Unit,
    private val moreClickListener: (String) -> Unit
) : ListAdapter<FileDocument, DocumentsHolder>(FILES_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsHolder {
        return DocumentsHolder.create(parent, onClickListener, moreClickListener)
    }

    override fun onBindViewHolder(holder: DocumentsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val FILES_DIFF_UTIL = object : DiffUtil.ItemCallback<FileDocument>() {

            override fun areItemsTheSame(oldItem: FileDocument, newItem: FileDocument): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: FileDocument, newItem: FileDocument): Boolean {
                return oldItem == newItem
            }
        }
    }
}