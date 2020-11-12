package gr.cpaleop.create_announcement.presentation.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import gr.cpaleop.create_announcement.databinding.FragmentContentBinding
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import gr.cpaleop.upload.domain.entities.UploadProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class AnnouncementContentGreekFragment :
    BaseApiFragment<FragmentContentBinding, CreateAnnouncementViewModel>(CreateAnnouncementViewModel::class) {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentContentBinding {
        return FragmentContentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.createAnnouncementContentTitleEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.addTitleGr(text.toString())
        }

        binding.createAnnouncementContentBodyEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.addTextGr(text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.uploadProgress.observe(viewLifecycleOwner, Observer(::handleProgress))
    }

    private fun handleProgress(uploadProgress: UploadProgress) {
        viewLifecycleOwner.lifecycleScope.launch {
            when (uploadProgress) {
                is UploadProgress.Idle -> {
                }
                is UploadProgress.Uploading -> disableInputs(true)
                UploadProgress.Success -> disableInputs(false)
                is UploadProgress.Failure -> disableInputs(false)
            }
        }
    }

    private fun disableInputs(shouldDisable: Boolean) {
        binding.createAnnouncementContentBodyEditText.isEnabled = !shouldDisable
        binding.createAnnouncementContentTitleEditText.isEnabled = !shouldDisable
    }
}