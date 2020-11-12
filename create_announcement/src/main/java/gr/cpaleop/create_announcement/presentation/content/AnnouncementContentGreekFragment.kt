package gr.cpaleop.create_announcement.presentation.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import gr.cpaleop.create_announcement.databinding.FragmentContentBinding
import gr.cpaleop.create_announcement.presentation.CreateAnnouncementViewModel
import gr.cpaleop.teithe_apps.presentation.base.BaseApiFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

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
    }

    private fun setupViews() {
        binding.createAnnouncementContentTitleEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.addTitleGr(text.toString())
        }

        binding.createAnnouncementContentBodyEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.addTextGr(text.toString())
        }
    }
}