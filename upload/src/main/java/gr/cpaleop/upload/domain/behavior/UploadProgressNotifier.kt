package gr.cpaleop.upload.domain.behavior

import gr.cpaleop.upload.domain.entities.UploadProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel

@FlowPreview
@ExperimentalCoroutinesApi
open class UploadProgressNotifier : BroadcastChannel<UploadProgress> by MutableUploadProgressNotifier