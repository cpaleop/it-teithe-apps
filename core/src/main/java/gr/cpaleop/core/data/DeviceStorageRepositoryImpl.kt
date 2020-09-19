package gr.cpaleop.core.data

import androidx.core.net.toUri
import gr.cpaleop.common.extensions.diff
import gr.cpaleop.core.data.mappers.DocumentMapper
import gr.cpaleop.core.data.model.local.AppDatabase
import gr.cpaleop.core.domain.behavior.DownloadFolder
import gr.cpaleop.core.domain.entities.Document
import gr.cpaleop.core.domain.repositories.DeviceStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.appendingSink
import okio.buffer
import timber.log.Timber
import java.io.File
import java.net.URI

class DeviceStorageRepositoryImpl(
    @DownloadFolder private val folder: File,
    private val appDatabase: AppDatabase,
    private val documentMapper: DocumentMapper
) : DeviceStorageRepository {

    override suspend fun saveFile(announcementId: String, fileName: String, fileData: ByteArray) =
        withContext(Dispatchers.IO) {
            val destinationFile = File(folder.absolutePath + "/" + fileName)
            val bufferedSink: BufferedSink = destinationFile.appendingSink().buffer()
            bufferedSink.write(fileData)
            try {
                bufferedSink.use {
                    it.write(fileData)
                }
                val insertedDocument = documentMapper(destinationFile, announcementId)
                appDatabase.documentDao().insert(insertedDocument)
            } catch (t: Throwable) {
                Timber.e(t)
            }
            return@withContext
        }

    override suspend fun getDocuments(): List<Document> = withContext(Dispatchers.IO) {
        val documentList = appDatabase.documentDao().fetchAll()
        val validatedDocuments = validateDocumentFiles(documentList)
        val obsoleteDocumentList = documentList.diff(validatedDocuments)
        /*deleteObsoleteFiles(obsoleteDocumentList)*/
        appDatabase.documentDao().deleteAll(obsoleteDocumentList)
        return@withContext validatedDocuments
    }

    override suspend fun getDocumentByUri(uri: String): Document {
        return appDatabase.documentDao().fetchByUri(uri)
    }

    override suspend fun deleteDocument(uri: String) {
        val deleted = File(URI(uri)).deleteRecursively()
        if (deleted) {
            val document = appDatabase.documentDao().fetchByUri(uri)
            appDatabase.documentDao().delete(document)
        }
    }

    override suspend fun renameDocument(uri: String, newName: String) {
        val originalFile = File(URI(uri))
        val originalFileAbsoluteDirectoryPath =
            originalFile.absolutePath.removeSuffix("/${originalFile.name}")
        val destinationFile = File("$originalFileAbsoluteDirectoryPath/$newName")

        val renamed = File(URI(uri)).renameTo(destinationFile)
        if (renamed) {
            val document = appDatabase.documentDao().fetchByUri(uri)
            appDatabase.documentDao().delete(document)
            val newDocument = document.copy(
                absolutePath = destinationFile.absolutePath,
                name = newName,
                uri = destinationFile.toUri().toString()
            )
            appDatabase.documentDao().insert(newDocument)
        }
    }

    private fun validateDocumentFiles(documentList: List<Document>): List<Document> {
        val validatedDocuments = mutableListOf<Document>()
        documentList.forEach { document ->
            if (File(document.absolutePath).exists()) validatedDocuments.add(document)
        }
        return validatedDocuments.toList()
    }

    private fun deleteObsoleteFiles(obsoleteDocumentList: List<Document>) {
        obsoleteDocumentList.forEach { document ->
            File(document.absolutePath).deleteRecursively()
        }
    }
}