package gr.cpaleop.documents.presentation.document

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class DocumentsAdapter(
    private val onLongClickListener: (String) -> Unit,
    private val onClickListener: (String, String) -> Unit,
    private val moreClickListener: (String) -> Unit
) : ListAdapter<FileDocument, DocumentsHolder>(FILES_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsHolder {
        return DocumentsHolder.create(
            parent,
            onLongClickListener,
            onClickListener,
            moreClickListener
        )
    }

    override fun onBindViewHolder(holder: DocumentsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(
        holder: DocumentsHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            payloads.forEach { bundle ->
                bundle as Bundle
                if (!bundle.getString(PAYLOAD_TITLE).isNullOrEmpty())
                    holder.bindTitle(currentList[position].name)
                else {
                    super.onBindViewHolder(holder, position, payloads)
                }
            }
        }
    }

    companion object {

        private const val PAYLOAD_TITLE = "PAYLOAD_TITLE"

        private val FILES_DIFF_UTIL = object : DiffUtil.ItemCallback<FileDocument>() {

            override fun areItemsTheSame(oldItem: FileDocument, newItem: FileDocument): Boolean {
                return oldItem.uri == newItem.uri
            }

            override fun areContentsTheSame(oldItem: FileDocument, newItem: FileDocument): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: FileDocument,
                newItem: FileDocument
            ): Any {
                return Bundle().apply {
                    if (oldItem.name != newItem.name) {
                        putString(
                            PAYLOAD_TITLE,
                            PAYLOAD_TITLE
                        )
                    }
                }
            }
        }
    }
}