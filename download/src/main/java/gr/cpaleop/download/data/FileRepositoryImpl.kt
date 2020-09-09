package gr.cpaleop.download.data

import gr.cpaleop.download.data.remote.DownloadApi
import gr.cpaleop.download.domain.entities.DownloadedFile
import gr.cpaleop.download.domain.repositories.FileRepository

class FileRepositoryImpl(
    private val downloadApi: DownloadApi,
    private val downloadedFileMapper: DownloadedFileMapper
) : FileRepository {

    override suspend fun getFile(fileId: String): DownloadedFile {
        return downloadedFileMapper(downloadApi.fetchFile(fileId))
    }
}