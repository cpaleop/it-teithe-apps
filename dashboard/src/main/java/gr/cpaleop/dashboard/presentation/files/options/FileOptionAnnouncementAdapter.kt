package gr.cpaleop.dashboard.presentation.files.options

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class FileOptionAnnouncementAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<FileOption, FileOptionHolder>(FILE_OPTION_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileOptionHolder {
        return FileOptionHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: FileOptionHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {

        private val FILE_OPTION_DIFF_UTIL = object : DiffUtil.ItemCallback<FileOption>() {

            override fun areItemsTheSame(oldItem: FileOption, newItem: FileOption): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FileOption, newItem: FileOption): Boolean {
                return oldItem == newItem
            }
        }
    }
}