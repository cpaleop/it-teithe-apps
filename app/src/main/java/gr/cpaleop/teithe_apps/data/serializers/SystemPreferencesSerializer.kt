package gr.cpaleop.teithe_apps.data.serializers

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import gr.cpaleop.teithe_apps.SystemPreferences
import java.io.InputStream
import java.io.OutputStream

object SystemPreferencesSerializer : Serializer<SystemPreferences> {

    override fun readFrom(input: InputStream): SystemPreferences {
        try {
            return SystemPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: SystemPreferences, output: OutputStream) = t.writeTo(output)
}