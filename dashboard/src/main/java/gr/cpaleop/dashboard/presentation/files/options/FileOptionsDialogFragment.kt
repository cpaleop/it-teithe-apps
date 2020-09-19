package gr.cpaleop.dashboard.presentation.files.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentFileOptionsBinding
import gr.cpaleop.dashboard.presentation.files.FilesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FileOptionsDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: FilesViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }
    private var _binding: DialogFragmentFileOptionsBinding? = null
    private val binding: DialogFragmentFileOptionsBinding get() = _binding!!
    private var fileOptionAdapter: FileOptionAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_file_options, container, false)
        _binding = DialogFragmentFileOptionsBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentDocumentDetails(navArgs<FileOptionsDialogFragmentArgs>().value.uri)
        viewModel.presentFileOptions()
    }

    private fun setupViews() {
        fileOptionAdapter = FileOptionAdapter(viewModel::handleFileOptionChoice)
        binding.fileOptionsRecyclerView.adapter = fileOptionAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            document.observe(viewLifecycleOwner, Observer(::updateTitle))
            fileOptions.observe(viewLifecycleOwner, Observer(::updateFileOptions))
            optionNavigateAnnouncement.observe(
                viewLifecycleOwner,
                Observer(::navigateToAnnouncement)
            )
            optionDelete.observe(
                viewLifecycleOwner,
                Observer(::showDeleteConfirmationDialog)
            )
            optionRename.observe(
                viewLifecycleOwner,
                Observer(::showRenameDialog)
            )
            optionInfo.observe(
                viewLifecycleOwner,
                Observer(::showInfoDialog)
            )
        }
    }

    private fun updateTitle(document: Document) {
        binding.fileOptionsFileNameTextView.text = document.name
    }

    private fun updateFileOptions(fileOptions: List<FileOption>) {
        fileOptionAdapter?.submitList(fileOptions)
    }

    private fun navigateToAnnouncement(announcementId: String) {
        dismiss()
        val directions =
            FileOptionsDialogFragmentDirections.fileOptionsDialogToAnnouncementActivity(
                announcementId
            )
        navController.navigate(directions)
    }

    private fun showDeleteConfirmationDialog(fileDeleteDetails: FileDetails) {
        MaterialDialog(requireContext())
            .lifecycleOwner(viewLifecycleOwner)
            .cancelOnTouchOutside(true)
            .title(R.string.files_delete_dialog_title, fileDeleteDetails.name)
            .positiveButton(R.string.profile_social_edit_submit)
            .message(R.string.files_delete_dialog_body)
            .positiveButton(R.string.files_delete_dialog_confirm) { materialDialog ->
                viewModel.deleteFile(fileDeleteDetails.uri)
                this@FileOptionsDialogFragment.dismiss()
                materialDialog.dismiss()
            }
            .negativeButton(R.string.files_delete_dialog_cancel) {
                it.cancel()
            }
            .show()
    }

    private fun showRenameDialog(fileRenameDetails: FileDetails) {
        MaterialDialog(requireContext())
            .lifecycleOwner(viewLifecycleOwner)
            .cancelOnTouchOutside(true)
            .title(R.string.files_rename_dialog_title)
            .positiveButton(R.string.profile_social_edit_submit)
            .input(prefill = fileRenameDetails.name) { materialDialog, input ->
                viewModel.renameFile(fileRenameDetails.uri, input.toString())
                this@FileOptionsDialogFragment.dismiss()
                materialDialog.dismiss()
            }
            .negativeButton(R.string.profile_social_edit_cancel) {
                it.cancel()
            }
            .show()
    }

    private fun showInfoDialog(fileUri: String) {
        TODO("Show file info dialog")
    }
}