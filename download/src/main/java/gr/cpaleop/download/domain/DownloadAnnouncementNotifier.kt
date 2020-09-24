package gr.cpaleop.download.domain

import gr.cpaleop.core.ColdStream
import gr.cpaleop.download.domain.entities.DownloadFileStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class DownloadAnnouncementNotifier : ColdStream<DownloadFileStatus>()