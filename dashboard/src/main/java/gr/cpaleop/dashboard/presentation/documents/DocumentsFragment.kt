package gr.cpaleop.dashboard.presentation.documents

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import gr.cpaleop.common.extensions.getMimeType
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentDocumentsBinding
import gr.cpaleop.dashboard.domain.entities.AnnouncementFolder
import gr.cpaleop.dashboard.domain.entities.DocumentPreview
import gr.cpaleop.dashboard.presentation.documents.announcement_folder.AnnouncementFolderAdapter
import gr.cpaleop.dashboard.presentation.documents.document.DocumentsAdapter
import gr.cpaleop.dashboard.presentation.documents.document.FileDocument
import gr.cpaleop.dashboard.presentation.documents.sort.DocumentSortOption
import gr.cpaleop.teithe_apps.di.Authority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.io.File

@ExperimentalCoroutinesApi
class DocumentsFragment : BaseFragment<FragmentDocumentsBinding>() {

    private val viewModel: DocumentsViewModel by viewModel()
    private val navController: NavController by lazy { findNavController() }

    @Authority
    private val authority: String by inject(named<Authority>())
    private val announcementId: String? by lazy { navArgs<DocumentsFragmentArgs>().value.announcementId }
    private var documentsAdapter: DocumentsAdapter? = null
    private var announcementFolderAdapter: AnnouncementFolderAdapter? = null
    private var drawableMap: MutableMap<Boolean, Drawable?>? = null
    private var documentPreviewDrawableResourceMap: Map<Int, Int> = mapOf(
        Pair(DocumentPreview.FILE, R.drawable.ic_view_list),
        Pair(DocumentPreview.FOLDER, R.drawable.ic_view_folder),
    )

    private val searchDocuments: (String) -> Unit = { query ->
        viewModel.searchDocuments(query)
    }

    private val searchAnnouncementFolders: (String) -> Unit = { query ->
        viewModel.searchAnnouncementFolders(query)
    }

    /**
     * The actual call to viewmodel in order to search content.
     *
     * Note: This is empty until a [DocumentPreview] has been set in the view model.
     * After a [DocumentPreview] has been set then the corresponding function will be set
     * via [setSearchFunction], eiter [searchDocuments] or [searchAnnouncementFolders]
     */
    private var search: (String) -> Unit = { }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDocumentsBinding {
        return FragmentDocumentsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.hideKeyboard()
        setupViews()
        observeViewModel()
        refreshViewState()
    }

    private fun setupViews() {
        drawableMap = mutableMapOf(
            Pair(true, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)),
            Pair(false, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up))
        )

        announcementFolderAdapter = AnnouncementFolderAdapter(::navigateToDocumentsFragment)
        documentsAdapter = DocumentsAdapter(::openFile, ::navigateToFileOptionsDialog)

        binding.documentsSwipeRefreshLayout.setOnRefreshListener {
            refreshViewState()
        }

        binding.filesSortingTextView.setOnClickListener {
            navigateToFileSortOptionsDialog()
        }

        if (announcementId == null) {
            binding.documentsPreviewImage.setOnClickListener {
                viewModel.togglePreview()
            }
        }

        binding.documentsSearchTextView.run {
            enableLeftDrawable(announcementId != null)
            doOnTextChanged { text, _, _, _ ->
                search(text.toString())
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
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            refresh.observe(viewLifecycleOwner, { refreshViewState() })
            documentPreview.observe(viewLifecycleOwner, Observer(::updatePreviewPreference))
            documents.observe(viewLifecycleOwner, Observer(::updateDocuments))
            documentsEmpty.observe(viewLifecycleOwner, Observer(::updateEmptyDocumentsView))
            documentSortOptionSelected.observe(viewLifecycleOwner, Observer(::updateSortView))
            documentAnnouncementFolders.observe(
                viewLifecycleOwner,
                Observer(::updateAnnouncementFolders)
            )
        }
    }

    /**
     * Convenient function to change search listener when document preview changes.
     * TODO: Maybe handle in viewModel
     * For more see [search]
     */
    private fun setSearchFunction(@DocumentPreview documentPreview: Int) {
        search = when (documentPreview) {
            DocumentPreview.FILE -> searchDocuments
            DocumentPreview.FOLDER -> searchAnnouncementFolders
            else -> { _ -> }
        }
    }

    private fun refreshViewState() {
        viewModel.presentDocuments(announcementId)
        viewModel.presentDocumentSortSelected()
    }

    private fun openFile(fileAbsolutePath: String) {
        val file = File(fileAbsolutePath)
        val uri = FileProvider.getUriForFile(requireContext(), authority, file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, file.getMimeType())
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooserIntent =
            Intent.createChooser(intent, context?.getString(R.string.documents_choose_file))
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(chooserIntent)
    }

    private fun navigateToDocumentsFragment(announcementId: String) {
        val directions = DocumentsFragmentDirections.documentsToDocuments(announcementId)
        navController.navigate(directions)
    }

    private fun navigateToFileOptionsDialog(fileUri: String) {
        val directions = DocumentsFragmentDirections.documentsToDocumentOptionsDialog(fileUri)
        navController.navigate(directions)
    }

    private fun navigateToFileSortOptionsDialog() {
        val directions = DocumentsFragmentDirections.documentsToDocumentSortOptionsDialog()
        navController.navigate(directions)
    }

    private fun updatePreviewPreference(@DocumentPreview documentPreview: Int) {
        setSearchFunction(documentPreview)
        binding.documentsPreviewImage.run {
            if (announcementId == null) {
                setImageResource(documentPreviewDrawableResourceMap[documentPreview] ?: return@run)
                if (!isVisible) isVisible = true
            }
        }
        when (documentPreview) {
            DocumentPreview.FILE -> {
                binding.documentsRecyclerView.run {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = documentsAdapter
                }
            }
            DocumentPreview.FOLDER -> {
                binding.documentsRecyclerView.run {
                    layoutManager = GridLayoutManager(requireContext(), 2)
                    adapter = announcementFolderAdapter
                }
            }
        }
    }

    private fun updateAnnouncementFolders(announcementFolders: List<AnnouncementFolder>) {
        announcementFolderAdapter?.submitList(announcementFolders) {
            binding.documentsRecyclerView.layoutManager?.scrollToPosition(0)
        }
    }

    private fun updateDocuments(documents: List<FileDocument>) {
        documentsAdapter?.submitList(documents) {
            binding.documentsRecyclerView.layoutManager?.scrollToPosition(0)
        }
    }

    private fun updateEmptyDocumentsView(documentsEmpty: Boolean) {
        binding.documentsEmptyTextView.run {
            text = requireContext().getString(R.string.documents_empty)
            isVisible = documentsEmpty && !binding.documentsSwipeRefreshLayout.isRefreshing
        }
    }

    private fun updateSortView(documentSortOption: DocumentSortOption) {
        binding.filesSortingTextView.run {
            isVisible = true
            setText(documentSortOption.labelResource)
            val drawable = drawableMap?.get(documentSortOption.descending)
            setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                drawable,
                null
            )
        }
    }

    private fun updateLoader(shouldLoad: Boolean) {
        binding.documentsSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}