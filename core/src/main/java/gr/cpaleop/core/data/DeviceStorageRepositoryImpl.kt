package gr.cpaleop.core.data

import gr.cpaleop.common.extensions.mapAsyncSuspended
import gr.cpaleop.core.data.mappers.DocumentMapper
import gr.cpaleop.core.domain.behavior.DownloadFolder
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.core.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.appendingSink
import okio.buffer
import java.io.File

class DeviceStorageRepositoryImpl(
    @DownloadFolder private val folder: File,
    private val documentMapper: DocumentMapper
) :
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

    override suspend fun getLocalDocuments(): List<Document> = withContext(Dispatchers.IO) {
        val files = folder.listFiles()?.toList() ?: emptyList()
        return@withContext files.mapAsyncSuspended(documentMapper::invoke)
    }
}