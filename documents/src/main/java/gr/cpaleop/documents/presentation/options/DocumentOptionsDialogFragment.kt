package gr.cpaleop.documents.presentation.options

import android.content.Intent
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
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.core.presentation.base.BaseBottomSheetDialog
import gr.cpaleop.documents.R
import gr.cpaleop.documents.databinding.DialogFragmentDocumentOptionsBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ExperimentalCoroutinesApi
class DocumentOptionsDialogFragment :
    BaseBottomSheetDialog<DialogFragmentDocumentOptionsBinding>() {

    private val viewModel: gr.cpaleop.documents.presentation.DocumentsViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }
    private var documentOptionAdapter: DocumentOptionAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogFragmentDocumentOptionsBinding {
        return DialogFragmentDocumentOptionsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentDocumentDetails(
            navArgs<DocumentOptionsDialogFragmentArgs>().value.uri
            /*arguments?.getString("uri") ?: ""*/
        )
        viewModel.presentDocumentOptions()
    }

    private fun setupViews() {
        documentOptionAdapter = DocumentOptionAdapter(viewModel::handleDocumentOptionChoice)
        binding.fileOptionsRecyclerView.adapter = documentOptionAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            document.observe(viewLifecycleOwner, Observer(::updateTitle))
            documentOptions.observe(viewLifecycleOwner, Observer(::updateFileOptions))
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
            optionShare.observe(
                viewLifecycleOwner,
                Observer(::shareFile)
            )
        }
    }

    private fun updateTitle(document: Document) {
        binding.fileOptionsFileNameTextView.text = document.name
    }

    private fun updateFileOptions(documentOptions: List<DocumentOption>) {
        documentOptionAdapter?.submitList(documentOptions)
    }

    private fun navigateToAnnouncement(announcementId: String) {
        dismiss()
        val directions =
            DocumentOptionsDialogFragmentDirections.documentOptionsDialogToAnnouncementActivity(
                announcementId
            )
        navController.navigate(directions)
    }

    private fun showDeleteConfirmationDialog(documentDeleteDetails: DocumentDetails) {
        MaterialDialog(requireContext())
            .lifecycleOwner(viewLifecycleOwner)
            .cancelOnTouchOutside(true)
            .title(R.string.documents_delete_dialog_title, documentDeleteDetails.name)
            .positiveButton(R.string.documents_edit_submit)
            .message(R.string.documents_delete_dialog_body)
            .positiveButton(R.string.documents_delete_dialog_confirm) { materialDialog ->
                viewModel.deleteDocument(documentDeleteDetails.uri)
                this@DocumentOptionsDialogFragment.dismiss()
                materialDialog.dismiss()
            }
            .negativeButton(R.string.documents_edit_cancel) {
                it.cancel()
            }
            .show()
    }

    private fun showRenameDialog(documentRenameDetails: DocumentDetails) {
        MaterialDialog(requireContext())
            .lifecycleOwner(viewLifecycleOwner)
            .cancelOnTouchOutside(true)
            .title(R.string.documents_rename_dialog_title)
            .positiveButton(R.string.documents_edit_submit)
            .input(prefill = documentRenameDetails.name) { materialDialog, input ->
                viewModel.renameDocument(documentRenameDetails.uri, input.toString())
                this@DocumentOptionsDialogFragment.dismiss()
                materialDialog.dismiss()
            }
            .negativeButton(R.string.documents_edit_cancel) {
                it.cancel()
            }
            .show()
    }

    private fun shareFile(documentShareOptionData: DocumentShareOptionData) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, documentShareOptionData.uri)
            type = documentShareOptionData.mimeType
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                resources.getText(R.string.documents_share_file_send_to)
            )
        )
        dismiss()
    }
}