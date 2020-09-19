package gr.cpaleop.dashboard.domain.repositories

import gr.cpaleop.core.domain.entities.Document

interface DeviceStorageRepository {

    suspend fun getLocalDocuments(): List<Document>

    suspend fun getLocalDocumentByUri(uri: String): Document
}