package gr.cpaleop.download.domain.repositories

import gr.cpaleop.download.domain.entities.DownloadedFile

interface DeviceStorageRepository {

    suspend fun saveFile(downloadedFile: DownloadedFile)
}