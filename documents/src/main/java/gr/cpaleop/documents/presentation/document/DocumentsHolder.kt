package gr.cpaleop.documents.presentation.document

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.documents.databinding.ItemDocumentBinding
import gr.cpaleop.teithe_apps.R as appR

class DocumentsHolder(
    private val binding: ItemDocumentBinding,
    private val onLongClickListener: (String) -> Unit,
    private val onClickListener: (String, String) -> Unit,
    private val moreClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FileDocument) {
        binding.run {
            root.run {
                setOnLongClickListener { onLongClickListener(item.uri); true }
                setOnClickListener { onClickListener(item.uri, item.absolutePath) }
            }

            documentConstraintLayoutParent.run {
                val color = if (item.isSelected) {
                    ContextCompat.getColor(root.context, appR.color.pink_200_40op)
                } else {
                    Color.TRANSPARENT
                }
                setBackgroundColor(color)
            }

            documentMoreImageView.run {
                /*isVisible = !item.inSelectionMode*/
                setOnClickListener { moreClickListener(item.uri) }
            }
            documentTitleTextView.text = item.name
            documentLastModifiedTextView.text = binding.root.context.getString(
                item.lastModifiedDate.labelRes,
                item.lastModifiedDate.dateHumanReadable
            )
            documentPreview.setImageResource(item.previewDrawable)
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            onLongClickListener: (String) -> Unit,
            onClickListener: (String, String) -> Unit,
            moreClickListener: (String) -> Unit
        ): DocumentsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDocumentBinding.inflate(layoutInflater, parent, false)
            return DocumentsHolder(
                binding,
                onLongClickListener,
                onClickListener,
                moreClickListener
            )
        }
    }
}