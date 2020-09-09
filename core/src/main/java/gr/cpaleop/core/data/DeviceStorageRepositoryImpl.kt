package gr.cpaleop.core.data

import gr.cpaleop.core.domain.behavior.DownloadFolder
import gr.cpaleop.core.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.appendingSink
import okio.buffer
import java.io.File

class DeviceStorageRepositoryImpl(@DownloadFolder private val folder: File) :
    DeviceStorageRepository {

    override suspend fun saveFile(fileName: String, fileData: ByteArray) =
        withContext(Dispatchers.IO) {
            val destinationFile = File(folder.absolutePath + "/" + fileName)
            val bufferedSink: BufferedSink = destinationFile.appendingSink().buffer()
            bufferedSink.write(fileData)
            bufferedSink.use {
                it.write(fileData)
            }
            return@withContext
        }
}