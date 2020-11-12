package gr.cpaleop.core.datasource.model.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RemoteAttachmentsConverter {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun jsonToObject(data: String?): List<String>? {
        if (data == null) return null
        return Json.decodeFromString<List<String>>(data)
    }

    @TypeConverter
    fun objectToJson(remotePublisher: List<String>?): String? {
        if (remotePublisher == null) return null
        return Json.encodeToString(remotePublisher)
    }
}