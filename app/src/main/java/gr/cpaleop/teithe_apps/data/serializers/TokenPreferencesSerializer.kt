package gr.cpaleop.teithe_apps.data.serializers

import androidx.datastore.CorruptionException
import androidx.datastore.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import gr.cpaleop.teithe_apps.TokenPreferences
import java.io.InputStream
import java.io.OutputStream

object TokenPreferencesSerializer : Serializer<TokenPreferences> {

    override fun readFrom(input: InputStream): TokenPreferences {
        try {
            return TokenPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(t: TokenPreferences, output: OutputStream) = t.writeTo(output)
}