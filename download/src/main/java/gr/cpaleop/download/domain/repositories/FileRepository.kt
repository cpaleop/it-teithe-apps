package gr.cpaleop.download.domain.repositories

import gr.cpaleop.download.domain.entities.DownloadedFile

interface FileRepository {

    suspend fun getFile(fileId: String): DownloadedFile
}