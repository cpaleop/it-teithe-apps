package gr.cpaleop.dashboard.presentation.files

import android.view.LayoutInflater
import android.view.ViewGroup
import gr.cpaleop.core.presentation.BaseFragment
import gr.cpaleop.dashboard.databinding.FragmentFilesBinding

class FilesFragment : BaseFragment<FragmentFilesBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilesBinding {
        return FragmentFilesBinding.inflate(inflater, container, false)
    }
}