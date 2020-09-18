package gr.cpaleop.dashboard.presentation.files.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import gr.cpaleop.dashboard.R
import gr.cpaleop.dashboard.databinding.DialogFragmentFileOptionsBinding
import gr.cpaleop.dashboard.presentation.files.FilesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FileOptionsDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: FilesViewModel by sharedViewModel()

    private var _binding: DialogFragmentFileOptionsBinding? = null
    private val binding: DialogFragmentFileOptionsBinding get() = _binding!!
    private var fileOptionAdapter: FileOptionAdapter? = null
    private var fileOptionAnnouncementAdapter: FileOptionAnnouncementAdapter? = null
    private var fileOptionConcatAdapter: MergeAdapter? = null

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
        viewModel.presentFileOptions()
    }

    private fun setupViews() {
        fileOptionAdapter = FileOptionAdapter {

        }

        fileOptionAnnouncementAdapter = FileOptionAnnouncementAdapter {

        }

        fileOptionConcatAdapter = MergeAdapter(fileOptionAnnouncementAdapter, fileOptionAdapter)
        binding.fileOptionsRecyclerView.adapter = fileOptionConcatAdapter
    }

    private fun observeViewModel() {
        viewModel.run {
            fileOptions.observe(viewLifecycleOwner, Observer(::updateFileOptions))
            fileOptionsAnnouncement.observe(
                viewLifecycleOwner,
                Observer(::updateFileOptionsAnnouncement)
            )
        }
    }

    private fun updateFileOptions(fileOptions: List<FileOption>) {
        fileOptionAdapter?.submitList(fileOptions)
    }

    private fun updateFileOptionsAnnouncement(fileOptions: List<FileOption>) {
        fileOptionAnnouncementAdapter?.submitList(fileOptions)
    }
}