package gr.cpaleop.upload.domain.behavior

import gr.cpaleop.upload.domain.entities.UploadProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow

@FlowPreview
@ExperimentalCoroutinesApi
internal object MutableUploadProgressNotifier : MutableStateFlow<UploadProgress> by
MutableStateFlow(UploadProgress.Idle) {

    fun notify(value: UploadProgress) {
        this.value = value
    }
}