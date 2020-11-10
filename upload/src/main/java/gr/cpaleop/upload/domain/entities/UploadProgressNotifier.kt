package gr.cpaleop.upload.domain.entities

import gr.cpaleop.core.HotStream
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class UploadProgressNotifier : HotStream<UploadProgress>()