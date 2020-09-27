package gr.cpaleop.announcements.presentation

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class AnnouncementsStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<AnnouncementsLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: AnnouncementsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): AnnouncementsLoadStateViewHolder {
        return AnnouncementsLoadStateViewHolder.create(parent, retry)
    }
}