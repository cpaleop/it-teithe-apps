package gr.cpaleop.core.data.model.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import gr.cpaleop.core.data.model.response.RemotePublisher

class RemotePublisherConverter {

    private val gson: Gson by lazy { Gson() }

    @TypeConverter
    fun jsonToObject(data: String?): RemotePublisher? {
        return gson.fromJson(data, RemotePublisher::class.java)
    }

    @TypeConverter
    fun objectToJson(remotePublisher: RemotePublisher?): String? {
        return gson.toJson(remotePublisher)
    }
}