package gr.cpaleop.upload.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import gr.cpaleop.core.data.remote.AnnouncementsApi
import gr.cpaleop.core.dispatchers.IODispatcher
import gr.cpaleop.upload.domain.entities.NewAnnouncement
import gr.cpaleop.upload.domain.repositories.AnnouncementsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AnnouncementsRepositoryImpl(
    @IODispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val applicationContext: Context,
    private val announcementsApi: AnnouncementsApi
) : AnnouncementsRepository {

    override suspend fun createAnnouncement(newAnnouncement: NewAnnouncement) =
        withContext(ioDispatcher) {
            var requestBody: RequestBody? = null
            var multiPartBody: MultipartBody.Part? = null
            var name = ""

            newAnnouncement.attachmentsUriList.forEach {
                val uri = Uri.parse(it)
                applicationContext.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bytes = inputStream.readBytes()
                    name = getFileName(uri)
                    requestBody = bytes.toRequestBody()
                }
            }

            requestBody?.let {
                multiPartBody = MultipartBody.Part.createFormData(
                    name = "uploads",
                    filename = name,
                    body = it
                )
            }

            announcementsApi.createAnnouncement(
                files = multiPartBody,
                title = newAnnouncement.title.gr.toRequestBody(),
                titleEn = newAnnouncement.title.en.toRequestBody(),
                text = newAnnouncement.text.gr.toRequestBody(),
                textEn = newAnnouncement.text.en.toRequestBody(),
                about = newAnnouncement.category.toRequestBody()
            )
        }

    private fun getFileName(uri: Uri): String {
        var name = ""
        applicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }
}