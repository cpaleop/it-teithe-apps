package gr.cpaleop.teithe_apps.data.serializers

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import gr.cpaleop.teithe_apps.DocumentPreviewPreferences
import java.io.InputStream
import java.io.OutputStream

object DocumentPreviewPreferencesSerializer : Serializer<DocumentPreviewPreferences> {

    override fun readFrom(input: InputStream): DocumentPreviewPreferences {
        try {
            return DocumentPreviewPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: DocumentPreviewPreferences, output: OutputStream) = t.writeTo(output)
}