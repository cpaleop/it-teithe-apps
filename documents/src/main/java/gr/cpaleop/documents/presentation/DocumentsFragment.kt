package gr.cpaleop.documents.presentation

import android.content.ActivityNotFoundException
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import gr.cpaleop.common.extensions.animateVisibiltyWithScale
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.common.extensions.setEndListener
import gr.cpaleop.common.extensions.setStartListener
import gr.cpaleop.core.domain.entities.DocumentPreview
import gr.cpaleop.core.presentation.Message
import gr.cpaleop.core.presentation.file_viewer.FileViewer
import gr.cpaleop.documents.R
import gr.cpaleop.documents.databinding.FragmentDocumentsBinding
import gr.cpaleop.documents.di.DocumentsKoinLoader
import gr.cpaleop.documents.presentation.announcement_folder.AnnouncementFolderAdapter
import gr.cpaleop.documents.presentation.announcement_folder.AnnouncementFolderPresentation
import gr.cpaleop.documents.presentation.document.DocumentsAdapter
import gr.cpaleop.documents.presentation.document.FileDocument
import gr.cpaleop.documents.presentation.options.DocumentDetails
import gr.cpaleop.documents.presentation.sort.DocumentSortOption
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import timber.log.Timber
import gr.cpaleop.teithe_apps.R as appR

@FlowPreview
@ExperimentalCoroutinesApi
class DocumentsFragment :
    BaseApiFragment<FragmentDocumentsBinding, DocumentsViewModel>(DocumentsViewModel::class) {

    private val navController: NavController by lazy { findNavController() }

    private val fileViewer: FileViewer by inject()
    private val announcementId: String? by lazy { navArgs<DocumentsFragmentArgs>().value.announcementId }
    private var documentsAdapter: DocumentsAdapter? = null
    private var announcementFolderAdapter: AnnouncementFolderAdapter? = null
    private var documentSortAnimatedDrawableMap: MutableMap<Boolean, AnimatedVectorDrawableCompat?>? =
        null
    private var documentSortDrawableMap: MutableMap<Boolean, Drawable?>? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var gridLayoutManager: GridLayoutManager
    private var documentPreviewAnimatedDrawableResourceMap: Map<Int, Int> = mapOf(
        Pair(DocumentPreview.FILE, R.drawable.folder_to_list_anim),
        Pair(DocumentPreview.FOLDER, R.drawable.list_to_folder_anim),
    )
    private var documentPreviewDrawableResourceMap: Map<Int, Int> = mapOf(
        Pair(DocumentPreview.FILE, R.drawable.ic_view_list),
        Pair(DocumentPreview.FOLDER, R.drawable.ic_view_folder),
    )
    private var inSelectionMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (announcementId != null) {
            enterTransition = null
            exitTransition = null
            reenterTransition = null
            returnTransition = null
            sharedElementEnterTransition = MaterialContainerTransform().apply {
                duration =
                    resources.getInteger(gr.cpaleop.teithe_apps.R.integer.shared_animation_duration)
                        .toLong()
                scrimColor = Color.TRANSPARENT
                containerColor = Color.TRANSPARENT
                fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
                isElevationShadowEnabled = false
                this.pathMotion = MaterialArcMotion()
            }
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDocumentsBinding {
        return FragmentDocumentsBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DocumentsKoinLoader.load()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        closeSelectionMode()
        DocumentsKoinLoader.unload()
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (announcementId != null) {
            binding.documentsRecyclerView.transitionName =
                "$SHARED_ELEMENT_CONTAINER_NAME$announcementId"
        }
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), appR.color.colorBackground)
        binding.root.hideKeyboard()
        setupViews()
        observeViewModel()
        viewModel.presentDocuments(announcementId)
        viewModel.presentDocumentSortSelected()
    }

    private fun setupViews() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
        documentSortAnimatedDrawableMap = mutableMapOf(
            Pair(
                true, AnimatedVectorDrawableCompat.create(
                    requireContext(),
                    R.drawable.sort_asc_to_desc_anim
                )
            ),
            Pair(
                false, AnimatedVectorDrawableCompat.create(
                    requireContext(),
                    R.drawable.sort_desc_to_asc_anin
                )
            )
        )
        documentSortDrawableMap = mutableMapOf(
            Pair(
                true, ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_arrow_down
                )
            ),
            Pair(
                false, ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_arrow_up
                )
            )
        )

        announcementFolderAdapter = AnnouncementFolderAdapter(::navigateToDocumentsFragment)
        documentsAdapter =
            DocumentsAdapter(
                ::longClickListener,
                ::openFile,
                ::navigateToFileOptionsDialog
            )

        binding.filesSortingTextView.setOnClickListener {
            closeSelectionMode()
            navigateToFileSortOptionsDialog()
        }

        if (announcementId == null) {
            binding.documentsPreviewImage.setOnClickListener {
                closeSelectionMode()
                viewModel.togglePreview()
            }
        }

        binding.documentsSearchTextView.run {
            enableLeftDrawable(announcementId != null)
            doOnTextChanged { text, _, _, _ ->
                viewModel.filter(text.toString())
            }
            setLeftDrawableListener {
                activity?.onBackPressed()
                return@setLeftDrawableListener true
            }
            setRightDrawableListener {
                text?.clear()
                clearFocus()
                binding.root.hideKeyboard()
                return@setRightDrawableListener true
            }
        }

        binding.documentsActionModeCloseActionImageView.setOnClickListener {
            closeSelectionMode()
        }

        binding.documentsActionModeSelectAllImageView.setOnClickListener {
            viewModel.selectAll()
        }

        binding.documentsActionModeDeleteAllImageView.setOnClickListener {
            viewModel.promptDeleteSelected()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            documentPreview.observe(viewLifecycleOwner, Observer(::updatePreviewPreference))
            documents.observe(viewLifecycleOwner, Observer(::updateDocuments))
            documentsAnySelected.observe(viewLifecycleOwner, Observer(::updateDeleteImage))
            documentsSelectedCounter.observe(
                viewLifecycleOwner,
                Observer(::updateActionModeCounter)
            )
            documentsEmpty.observe(viewLifecycleOwner, Observer(::updateEmptyDocumentsView))
            documentSortOptionSelected.observe(viewLifecycleOwner, Observer(::updateSortView))
            documentAnnouncementFolders.observe(
                viewLifecycleOwner,
                Observer(::updateAnnouncementFolders)
            )
            optionDelete.observe(
                viewLifecycleOwner,
                Observer(::showDeleteConfirmationDialog)
            )
        }
    }

    private fun updateDeleteImage(isAnySelected: Boolean) {
        binding.documentsActionModeDeleteAllImageView.run {
            isEnabled = isAnySelected
            val alphaIndicator = if (isAnySelected) 1f else 0.3f
            animate()
                .setDuration(200)
                .alpha(alphaIndicator)
                .start()
        }
    }

    private fun updateActionModeCounter(selectedCounter: Int) {
        binding.documentsActionModeTitleTextView.text =
            if (selectedCounter > 0) {
                getString(R.string.documents_action_title_selected, selectedCounter)
            } else {
                getString(R.string.documents_action_title)
            }
    }

    private fun longClickListener(documentUri: String): Boolean {
        viewModel.updateSelection(documentUri)
        startSelectionMode()
        return true
    }

    private fun startSelectionMode() {
        inSelectionMode = true
        binding.run {
            documentsSearchTextView.animate()
                .setDuration(200)
                .alpha(0f)
                .setEndListener { documentsSearchTextView.visibility = View.INVISIBLE }
                .start()

            documentsActionModeLayout.animate()
                .setDuration(200)
                .alpha(1f)
                .setStartListener { documentsActionModeLayout.visibility = View.VISIBLE }
                .start()
        }
    }

    private fun closeSelectionMode() {
        if (inSelectionMode) {
            inSelectionMode = false
            binding.run {
                documentsActionModeLayout.animate()
                    .setDuration(200)
                    .alpha(0f)
                    .setEndListener { documentsActionModeLayout.visibility = View.INVISIBLE }
                    .start()

                documentsSearchTextView.animate()
                    .setDuration(200)
                    .alpha(1f)
                    .setEndListener { documentsSearchTextView.visibility = View.VISIBLE }
                    .start()
            }
            viewModel.clearSelections()
        }
    }

    private fun openFile(documentUri: String, fileAbsolutePath: String) {
        try {
            if (inSelectionMode) {
                viewModel.updateSelection(documentUri)
            } else {
                fileViewer(requireContext(), documentUri)
                /*val file = File(fileAbsolutePath)
                val uri = FileProvider.getUriForFile(requireContext(), authority, file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, file.getMimeType())
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                val chooserIntent =
                    Intent.createChooser(intent, context?.getString(R.string.documents_choose_file))
                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(chooserIntent)*/
            }
        } catch (t: ActivityNotFoundException) {
            Timber.e(t)
            showSnackbarMessage(Message(R.string.documents_file_navigation_error))
        }
    }

    private fun navigateToDocumentsFragment(view: View, announcementId: String) {
        view.transitionName = "shared_element_container$announcementId"
        val extras =
            FragmentNavigatorExtras(view to "$SHARED_ELEMENT_CONTAINER_NAME$announcementId")
        val bundle = Bundle().apply {
            putString("announcementId", announcementId)
        }
        navController.navigate(R.id.documentsToDocuments, bundle, null, extras)
    }

    private fun navigateToFileOptionsDialog(fileUri: String) {
        closeSelectionMode()
        val directions = DocumentsFragmentDirections.documentsToDocumentOptionsDialog(fileUri)
        navController.navigate(directions)
    }

    private fun navigateToFileSortOptionsDialog() {
        val directions = DocumentsFragmentDirections.documentsToDocumentSortOptionsDialog()
        navController.navigate(directions)
    }

    private fun updatePreviewPreference(@DocumentPreview documentPreview: Int) {
        binding.documentsSearchTextView.setText("")
        togglePreview(documentPreview)
        toggleAdapter(documentPreview)
    }

    private fun togglePreview(@DocumentPreview documentPreview: Int) {
        binding.documentsPreviewImage.run {
            if (announcementId == null) {
                if (!isVisible) isVisible = true
                val shouldAnimate = this.drawable != null
                if (shouldAnimate) {
                    setImageResource(
                        documentPreviewAnimatedDrawableResourceMap[documentPreview] ?: return@run
                    )
                    when (val drawable = this.drawable) {
                        is AnimatedVectorDrawableCompat -> {
                            if (!drawable.isRunning)
                                drawable.start()
                        }
                        is AnimatedVectorDrawable -> {
                            if (!drawable.isRunning)
                                drawable.start()
                        }
                    }
                } else {
                    setImageResource(
                        documentPreviewDrawableResourceMap[documentPreview] ?: return@run
                    )
                }
            }
        }
    }

    private fun toggleAdapter(@DocumentPreview documentPreview: Int) {
        when (documentPreview) {
            DocumentPreview.FILE -> {
                binding.documentsRecyclerView.run {
                    layoutManager = linearLayoutManager
                    adapter = documentsAdapter
                }
            }
            DocumentPreview.FOLDER -> {
                binding.documentsRecyclerView.run {
                    layoutManager = gridLayoutManager
                    adapter = announcementFolderAdapter
                }
            }
        }
    }

    private fun updateAnnouncementFolders(announcementFolders: List<AnnouncementFolderPresentation>) {
        announcementFolderAdapter?.submitList(announcementFolders)
    }

    private fun updateDocuments(documents: List<FileDocument>) {
        documentsAdapter?.submitList(documents)
    }

    private fun updateEmptyDocumentsView(documentsEmpty: Boolean) {
        binding.documentsEmptyImageView.animateVisibiltyWithScale(documentsEmpty).start()
        binding.documentsEmptyTextView.animateVisibiltyWithScale(documentsEmpty).start()
    }

    private fun updateSortView(documentSortOption: DocumentSortOption) {
        binding.filesSortingTextView.run {
            isVisible = true
            setText(documentSortOption.labelResource)

            if (this.compoundDrawables[2] == null) {
                val drawable = documentSortDrawableMap?.get(documentSortOption.descending)
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
            } else {
                val drawable = documentSortAnimatedDrawableMap?.get(documentSortOption.descending)
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
                (this.compoundDrawables[2] as AnimatedVectorDrawableCompat?)?.start()
            }
        }
    }

    private fun showDeleteConfirmationDialog(documentDeleteDetails: DocumentDetails) {
        val titleResource =
            if (documentDeleteDetails.uriList.size > 1) {
                R.string.documents_delete_multiple_dialog_title
            } else {
                R.string.documents_delete_dialog_title
            }

        val title = getString(titleResource, documentDeleteDetails.name)
        MaterialDialog(requireContext())
            .lifecycleOwner(viewLifecycleOwner)
            .cancelOnTouchOutside(true)
            .title(text = title)
            .positiveButton(R.string.documents_edit_submit)
            .message(R.string.documents_delete_dialog_body)
            .positiveButton(R.string.documents_delete_dialog_confirm) {
                viewModel.deleteDocuments(documentDeleteDetails.uriList)
            }
            .negativeButton(R.string.documents_edit_cancel) {
                it.cancel()
            }
            .show()
    }

    companion object {

        private const val SHARED_ELEMENT_CONTAINER_NAME = "shared_element_container"
    }
}