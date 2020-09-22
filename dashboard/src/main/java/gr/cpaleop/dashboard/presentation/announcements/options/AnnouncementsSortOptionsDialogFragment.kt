package gr.cpaleop.dashboard.presentation.announcements.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import gr.cpaleop.core.presentation.BaseBottomSheetDialog
import gr.cpaleop.dashboard.databinding.DialogFragmentAnnouncementsSortOptionsBinding
import gr.cpaleop.dashboard.presentation.announcements.AnnouncementsViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AnnouncementsSortOptionsDialogFragment :
    BaseBottomSheetDialog<DialogFragmentAnnouncementsSortOptionsBinding>() {

    private val viewModel: AnnouncementsViewModel by sharedViewModel()
    private var announcementsSortOptionsAdapter: AnnouncementsSortOptionsAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentAnnouncementsSortOptionsBinding {
        return DialogFragmentAnnouncementsSortOptionsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentSortOptions()
    }

    private fun setupViews() {
        announcementsSortOptionsAdapter =
            AnnouncementsSortOptionsAdapter(viewModel::updateAnnouncementSort)
        binding.sortOptionsRecyclerView.adapter = announcementsSortOptionsAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            dismissSortOptionsDialog.observe(viewLifecycleOwner, Observer { dismiss() })
            options.observe(viewLifecycleOwner, Observer(::showSortOptions))
        }
    }

    private fun showSortOptions(optionList: List<AnnouncementSortOption>) {
        announcementsSortOptionsAdapter?.submitList(optionList)
    }

    companion object {

        const val OPTIONS_DIALOG_NAME = "OPTIONS_DIALOG_NAME"
    }
}