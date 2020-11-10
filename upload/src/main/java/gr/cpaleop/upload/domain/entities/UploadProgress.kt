package gr.cpaleop.upload.domain.entities

sealed class UploadProgress {

    object Uploading : UploadProgress()

    object Success : UploadProgress()

    data class Failure(val throwable: Throwable) : UploadProgress()
}