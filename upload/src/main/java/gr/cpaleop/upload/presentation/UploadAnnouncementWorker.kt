package gr.cpaleop.upload.presentation

import android.content.Context
import androidx.work.*
import gr.cpaleop.upload.domain.behavior.MutableUploadProgressNotifier
import gr.cpaleop.upload.domain.entities.MultilanguageText
import gr.cpaleop.upload.domain.entities.NewAnnouncement
import gr.cpaleop.upload.domain.entities.UploadProgress
import gr.cpaleop.upload.domain.usecases.CreateAnnouncementUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class UploadAnnouncementWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val uploadNotificationManager: UploadNotificationManager by inject()
    private val uploadProgressNotifier = MutableUploadProgressNotifier
    private val createAnnouncementUseCase: CreateAnnouncementUseCase by inject()

    override suspend fun doWork(): Result {
        return try {
            val newAnnouncement = handleInput() ?: throw IllegalArgumentException("Invalid worker parameters")
            uploadProgressNotifier.notify(UploadProgress.Uploading)
            setupNotification()
            createAnnouncementUseCase(newAnnouncement)
            uploadNotificationManager.showSuccess()
            uploadProgressNotifier.notify(UploadProgress.Success)
            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            uploadNotificationManager.showFailure()
            uploadProgressNotifier.notify(UploadProgress.Failure(t))
            Result.failure()
        }
    }

    private fun handleInput(): NewAnnouncement? {
        val announcementTitle = workerParameters.inputData.getString(ANNOUNCEMENT_TITLE) ?: return null
        val announcementTitleEn = workerParameters.inputData.getString(ANNOUNCEMENT_TITLE_EN) ?: return null
        val announcementText = workerParameters.inputData.getString(ANNOUNCEMENT_TEXT) ?: return null
        val announcementTextEn = workerParameters.inputData.getString(ANNOUNCEMENT_TEXT_EN) ?: return null
        val announcementCategoryId = workerParameters.inputData.getString(ANNOUNCEMENT_CATEGORY_ID) ?: return null
        val announcementAttachmentUriList =
            workerParameters.inputData.getStringArray(ANNOUNCEMENT_ATTACHMENTS_URI_LIST) ?: return null

        return NewAnnouncement(
            title = MultilanguageText(gr = announcementTitle, en = announcementTitleEn),
            text = MultilanguageText(gr = announcementText, en = announcementTextEn),
            category = announcementCategoryId,
            attachmentsUriList = announcementAttachmentUriList.toList()
        )
    }

    private suspend fun setupNotification() {
        val notificationId = uploadNotificationManager.notificationId
        val notification = uploadNotificationManager.notification
        val foregroundInfo = ForegroundInfo(notificationId, notification)
        setForeground(foregroundInfo)
    }

    companion object {

        const val ANNOUNCEMENT_TITLE = "ANNOUNCEMENT_TITLE"
        const val ANNOUNCEMENT_TITLE_EN = "ANNOUNCEMENT_TITLE_EN"
        const val ANNOUNCEMENT_TEXT = "ANNOUNCEMENT_TEXT"
        const val ANNOUNCEMENT_TEXT_EN = "ANNOUNCEMENT_TEXT_EN"
        const val ANNOUNCEMENT_CATEGORY_ID = "ANNOUNCEMENT_CATEGORY_ID"
        const val ANNOUNCEMENT_ATTACHMENTS_URI_LIST = "ANNOUNCEMENT_ATTACHMENTS_URI_LIST"

        fun enqueue(context: Context, newAnnouncement: NewAnnouncement) {
            val inputData = Data.Builder().apply {
                putString(ANNOUNCEMENT_TITLE, newAnnouncement.title.gr)
                putString(ANNOUNCEMENT_TITLE_EN, newAnnouncement.title.en)
                putString(ANNOUNCEMENT_TEXT, newAnnouncement.text.gr)
                putString(ANNOUNCEMENT_TEXT_EN, newAnnouncement.text.en)
                putString(ANNOUNCEMENT_CATEGORY_ID, newAnnouncement.category)
                putStringArray(ANNOUNCEMENT_ATTACHMENTS_URI_LIST, newAnnouncement.attachmentsUriList.toTypedArray())
            }.build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val downloadWorkRequest = OneTimeWorkRequestBuilder<UploadAnnouncementWorker>()
                .setInputData(inputData)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context).enqueue(downloadWorkRequest)
        }
    }
}