package gr.cpaleop.core.data.model.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RemoteAttachmentsConverter {

    private val gson: Gson by lazy { Gson() }

    @TypeConverter
    fun jsonToObject(data: String?): List<String>? {
        val listType: Type =
            object : TypeToken<List<String?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun objectToJson(remotePublisher: List<String>?): String? {
        return gson.toJson(remotePublisher)
    }
}