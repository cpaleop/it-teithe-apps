package gr.cpaleop.upload.domain.behavior

import gr.cpaleop.upload.domain.entities.UploadProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow

@FlowPreview
@ExperimentalCoroutinesApi
open class UploadProgressNotifier : StateFlow<UploadProgress> by MutableUploadProgressNotifier