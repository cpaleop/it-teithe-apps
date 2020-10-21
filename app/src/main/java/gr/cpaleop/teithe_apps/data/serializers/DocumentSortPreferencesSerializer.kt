package gr.cpaleop.teithe_apps.data.serializers

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import gr.cpaleop.teithe_apps.DocumentSortPreferences
import java.io.InputStream
import java.io.OutputStream

object DocumentSortPreferencesSerializer : Serializer<DocumentSortPreferences> {

    override fun readFrom(input: InputStream): DocumentSortPreferences {
        try {
            return DocumentSortPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: DocumentSortPreferences, output: OutputStream) = t.writeTo(output)
}