package gr.cpaleop.upload.domain.behavior

import gr.cpaleop.upload.domain.entities.UploadProgress
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel

@FlowPreview
@ExperimentalCoroutinesApi
internal object MutableUploadProgressNotifier : BroadcastChannel<UploadProgress> by
BroadcastChannel(2) {

    suspend fun notify(element: UploadProgress) {
        this.send(element)
        if (element == UploadProgress.Success) {
            this.send(UploadProgress.Idle)
        }
    }
}