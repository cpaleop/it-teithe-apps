package gr.cpaleop.download.data

import gr.cpaleop.core.data.model.response.RemoteFile
import gr.cpaleop.download.domain.entities.DownloadedFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadedFileMapper {

    suspend operator fun invoke(remoteFile: RemoteFile): DownloadedFile =
        withContext(Dispatchers.Default) {
            DownloadedFile(
                name = remoteFile.name ?: "",
                contentType = remoteFile.contentType ?: "",
                data = remoteFile.data?.data ?: emptyList()
            )
        }
}