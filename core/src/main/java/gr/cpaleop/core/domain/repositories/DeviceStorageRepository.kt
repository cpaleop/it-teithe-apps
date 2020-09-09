package gr.cpaleop.core.domain.repositories

interface DeviceStorageRepository {

    suspend fun saveFile(fileName: String, fileData: ByteArray)
}