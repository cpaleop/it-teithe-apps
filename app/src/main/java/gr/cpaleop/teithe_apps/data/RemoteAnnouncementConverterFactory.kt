package gr.cpaleop.teithe_apps.data

import com.google.gson.Gson
import gr.cpaleop.core.data.model.response.RemoteAnnouncement
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class RemoteAnnouncementConverterFactory(
    private val remoteAnnouncementMapper: RemoteAnnouncementMapper,
    private val gson: Gson,
    private val gsonConverterFactory: GsonConverterFactory
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation?>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val clazz = getRawType(type)
        return if (clazz == RemoteAnnouncement::class.java) {
            RemoteAnnouncementConverter(remoteAnnouncementMapper, gson)
        } else {
            gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return gsonConverterFactory.requestBodyConverter(
            type,
            parameterAnnotations,
            methodAnnotations,
            retrofit
        )
    }

    companion object {

        fun create(
            remoteAnnouncementMapper: RemoteAnnouncementMapper,
            gson: Gson,
            gsonConverterFactory: GsonConverterFactory
        ): RemoteAnnouncementConverterFactory {
            return RemoteAnnouncementConverterFactory(
                remoteAnnouncementMapper,
                gson,
                gsonConverterFactory
            )
        }
    }
}