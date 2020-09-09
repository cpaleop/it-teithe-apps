package gr.cpaleop.download.data

import gr.cpaleop.download.di.DownloadFolder
import gr.cpaleop.download.domain.entities.DownloadedFile
import gr.cpaleop.download.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.appendingSink
import okio.buffer
import java.io.File

class DeviceStorageRepositoryImpl(@DownloadFolder private val folder: File) :
    DeviceStorageRepository {

    override suspend fun saveFile(downloadedFile: DownloadedFile) = withContext(Dispatchers.IO) {
        val destinationFile = File(folder.absolutePath + "/" + downloadedFile.name)
        val bufferedSink: BufferedSink = destinationFile.appendingSink().buffer()
        bufferedSink.write(downloadedFile.data.toByteArray())
        bufferedSink.use {
            it.write(downloadedFile.data.toByteArray())
        }
        return@withContext
    }
}