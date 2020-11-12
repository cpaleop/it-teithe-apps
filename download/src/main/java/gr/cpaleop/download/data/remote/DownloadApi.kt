package gr.cpaleop.download.data.remote

import gr.cpaleop.core.datasource.model.response.RemoteFile
import retrofit2.http.GET
import retrofit2.http.Path

interface DownloadApi {

    @GET("files/{fileId}")
    suspend fun fetchFile(@Path("fileId") fileId: String?): RemoteFile
}