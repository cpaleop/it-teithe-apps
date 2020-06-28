package gr.cpaleop.dashboard.presentation.announcements

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.ItemLoadStateFooterBinding

class AnnouncementsLoadStateViewHolder(
    private val binding: ItemLoadStateFooterBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): AnnouncementsLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state_footer, parent, false)
            val binding = ItemLoadStateFooterBinding.bind(view)
            return AnnouncementsLoadStateViewHolder(binding, retry)
        }
    }
}