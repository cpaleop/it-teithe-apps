package gr.cpaleop.dashboard.presentation.files

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class FilesAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<FileDocument, FilesHolder>(FILES_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesHolder {
        return FilesHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: FilesHolder, position: Int) {
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