package gr.cpaleop.dashboard.presentation.files

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import gr.cpaleop.common.extensions.getMimeType
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.FragmentFilesBinding
import gr.cpaleop.teithe_apps.di.Authority
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.io.File

class FilesFragment : BaseFragment<FragmentFilesBinding>() {

    private val viewModel: FilesViewModel by viewModel()
    @Authority
    private val authority: String by inject(named<Authority>())
    private var filesAdapter: FilesAdapter? = null

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilesBinding {
        return FragmentFilesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
        viewModel.presentDocuments()
    }

    private fun setupViews() {
        filesAdapter = FilesAdapter(::openFile)
        binding.documentsRecyclerView.adapter = filesAdapter

        binding.documentsSwipeRefreshLayout.setOnRefreshListener {
            viewModel.presentDocuments()
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            loading.observe(viewLifecycleOwner, Observer(::toggleLoad))
            documents.observe(viewLifecycleOwner, Observer(::showDocuments))
        }
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

    private fun showDocuments(documents: List<FileDocument>) {
        filesAdapter?.submitList(documents)
    }

    private fun toggleLoad(shouldLoad: Boolean) {
        binding.documentsSwipeRefreshLayout.isRefreshing = shouldLoad
    }
}