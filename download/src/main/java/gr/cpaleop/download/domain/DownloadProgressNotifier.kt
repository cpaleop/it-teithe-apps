package gr.cpaleop.download.domain

import gr.cpaleop.core.HotStream
import gr.cpaleop.download.domain.entities.DownloadProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class DownloadProgressNotifier : HotStream<DownloadProgress>()