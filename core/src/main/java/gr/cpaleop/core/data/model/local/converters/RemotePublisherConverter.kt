package gr.cpaleop.core.data.model.local.converters

import androidx.room.TypeConverter
import gr.cpaleop.core.data.model.response.RemotePublisher
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RemotePublisherConverter {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun jsonToObject(data: String?): RemotePublisher? {
        if (data == null) return null
        return json.decodeFromString<RemotePublisher>(data)
    }

    @TypeConverter
    fun objectToJson(remotePublisher: RemotePublisher?): String? {
        if (remotePublisher == null) return null
        return json.encodeToString(remotePublisher)
    }
}