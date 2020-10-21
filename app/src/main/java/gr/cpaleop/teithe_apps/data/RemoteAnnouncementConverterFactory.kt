package gr.cpaleop.teithe_apps.data

import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import kotlinx.serialization.json.Json
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class RemoteAnnouncementConverterFactory(
    private val remoteAnnouncementMapper: RemoteAnnouncementMapper,
    private val converterFactory: Converter.Factory,
    private val json: Json
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation?>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val clazz = getRawType(type)
        return if (clazz == RemoteAnnouncement::class.java) {
            RemoteAnnouncementConverter(json, remoteAnnouncementMapper)
        } else {
            converterFactory.responseBodyConverter(type, annotations, retrofit)
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return converterFactory.requestBodyConverter(
            type,
            parameterAnnotations,
            methodAnnotations,
            retrofit
        )
    }

    companion object {

        fun create(
            remoteAnnouncementMapper: RemoteAnnouncementMapper,
            converterFactory: Converter.Factory,
            json: Json
        ): RemoteAnnouncementConverterFactory {
            return RemoteAnnouncementConverterFactory(
                remoteAnnouncementMapper,
                converterFactory,
                json
            )
        }
    }
}