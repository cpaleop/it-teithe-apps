package gr.cpaleop.dashboard.presentation.files

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
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import gr.cpaleop.common.OnCompoundDrawableClickListener
import gr.cpaleop.common.extensions.getMimeType
import gr.cpaleop.common.extensions.hideKeyboard
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentFilesBinding
import gr.cpaleop.dashboard.presentation.files.sort.DocumentSortOption
import gr.cpaleop.teithe_apps.di.Authority
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named
import java.io.File
import gr.cpaleop.teithe_apps.R as appR

class FilesFragment : BaseFragment<FragmentFilesBinding>() {

    private val viewModel: DocumentsViewModel by sharedViewModel()
    private val navController: NavController by lazy { findNavController() }

    @Authority
    private val authority: String by inject(named<Authority>())
    private var filesAdapter: FilesAdapter? = null
    private var hasSearchViewAnimatedToCancel: Boolean = false
    private var hasSearchViewAnimatedToSearch: Boolean = false
    private var submitListCallbackAction: () -> Unit = {}
    private var drawableMap: MutableMap<Boolean, Drawable?>? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilesBinding {
        return FragmentFilesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.hideKeyboard()
        setupViews()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        refreshViewState()
    }

    private fun setupViews() {
        drawableMap = mutableMapOf(
            Pair(true, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_down)),
            Pair(false, ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_up))
        )

        filesAdapter = FilesAdapter(::openFile, ::navigateToFileOptionsDialog)
        binding.documentsRecyclerView.adapter = filesAdapter

        binding.documentsSwipeRefreshLayout.setOnRefreshListener {
            binding.documentsSearchTextView.setText(requireContext().getString(appR.string.empty))
            refreshViewState()
        }

        binding.filesSortingTextView.setOnClickListener {
            navigateToFileSortOptionsDialog()
        }

        binding.documentsSearchTextView.run {
            val endDrawable = AnimatedVectorDrawableCompat.create(
                requireContext(),
                appR.drawable.search_to_cancel
            )
            setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                endDrawable,
                null
            )

            setOnTouchListener(
                OnCompoundDrawableClickListener(OnCompoundDrawableClickListener.DRAWABLE_RIGHT) {
                    text.clear()
                    clearFocus()
                    binding.root.hideKeyboard()
                    return@OnCompoundDrawableClickListener true
                }
            )

            doOnTextChanged { text, _, _, _ ->
                if (text != null) {
                    viewModel.searchDocuments(text.toString())
                    submitListCallbackAction = if (text.isNotEmpty()) {
                        { binding.documentsRecyclerView.smoothScrollToPosition(0) }
                    } else {
                        {}
                    }

                    var animDrawable: AnimatedVectorDrawableCompat? = null
                    if (text.isEmpty()) {
                        (compoundDrawables[2] as Animatable2Compat).apply {
                            if (!hasSearchViewAnimatedToSearch) {
                                animDrawable = AnimatedVectorDrawableCompat.create(
                                    requireContext(),
                                    appR.drawable.cancel_to_search
                                )
                                setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    animDrawable,
                                    null
                                )

                                animDrawable?.start()
                                hasSearchViewAnimatedToCancel = false
                                hasSearchViewAnimatedToSearch = !hasSearchViewAnimatedToSearch
                            }
                        }
                    } else {
                        (compoundDrawables[2] as Animatable2Compat).apply {
                            if (!hasSearchViewAnimatedToCancel) {
                                animDrawable = AnimatedVectorDrawableCompat.create(
                                    requireContext(),
                                    appR.drawable.search_to_cancel
                                )
                                setCompoundDrawablesWithIntrinsicBounds(
                                    null,
                                    null,
                                    animDrawable,
                                    null
                                )

                                animDrawable?.start()
                                hasSearchViewAnimatedToSearch = false
                                hasSearchViewAnimatedToCancel = !hasSearchViewAnimatedToCancel
                            }
                        }
                    }
                } else {
                    submitListCallbackAction = {}
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::updateLoader))
            refresh.observe(viewLifecycleOwner, Observer {
                refreshViewState()
            })
            documents.observe(viewLifecycleOwner, Observer(::updateDocuments))
            documentsEmpty.observe(viewLifecycleOwner, Observer(::updateEmptyDocumentsView))
            documentsFilterEmpty.observe(
                viewLifecycleOwner,
                Observer(::updateDocumentsNotFoundView)
            )
            documentSortOptionSelected.observe(viewLifecycleOwner, Observer(::updateSortView))
        }
    }

    private fun refreshViewState() {
        viewModel.presentDocuments()
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
            Intent.createChooser(intent, context?.getString(R.string.files_choose_file))
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(chooserIntent)
    }

    private fun navigateToFileOptionsDialog(fileUri: String) {
        val directions = FilesFragmentDirections.filesToFileOptionsDialog(fileUri)
        navController.navigate(directions)
    }

    private fun navigateToFileSortOptionsDialog() {
        val directions = FilesFragmentDirections.filesToFileSortOptionsDialog()
        navController.navigate(directions)
    }

    private fun updateDocuments(documents: List<FileDocument>) {
        filesAdapter?.submitList(documents) {
            submitListCallbackAction()
        }
    }

    private fun updateEmptyDocumentsView(documentsEmpty: Boolean) {
        binding.documentsEmptyTextView.run {
            text = requireContext().getString(R.string.files_empty)
            isVisible = documentsEmpty
        }
    }

    private fun updateDocumentsNotFoundView(documentsNotFound: Boolean) {
        binding.documentsEmptyTextView.run {
            text = requireContext().getString(R.string.files_not_found)
            isVisible = documentsNotFound
        }
    }

    private fun updateSortView(documentSortOption: DocumentSortOption) {
        binding.filesSortingTextView.run {
            setText(documentSortOption.label)
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