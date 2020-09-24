package gr.cpaleop.download.presentation

import android.content.Context
import androidx.work.*
import gr.cpaleop.download.domain.DownloadProgressNotifier
import gr.cpaleop.download.domain.usecases.DownloadFileUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class DownloadFileWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val workerCoroutineScope = CoroutineScope(Dispatchers.Default)
    private val downloadFilesUseCase: DownloadFileUseCase by inject()
    private val downloadNotificationManager: DownloadNotificationManager by inject()
    private val downloadProgressNotifier: DownloadProgressNotifier by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val files = workerParameters.inputData.getStringArray(DATA_FILE_LIST)?.toList()
            ?: return@coroutineScope Result.failure()

        val announcementId = workerParameters.inputData.getString(DATA_ANNOUNCEMENT_ID)
            ?: return@coroutineScope Result.failure()

        setupNotification(files.size)
        try {
            observeProgress()
            downloadFilesUseCase(announcementId, files)
            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }
    }

    private fun observeProgress() {
        workerCoroutineScope.launch {
            downloadProgressNotifier.asFlow()
                .collect { downloadProgress ->
                    if (downloadProgress.current != downloadProgress.total) {
                        downloadNotificationManager.showProgress(downloadProgress)
                    } else {
                        downloadNotificationManager.cancelProgress()
                        downloadNotificationManager.showSuccess(downloadProgress.current)
                    }
                }
        }
    }

    private suspend fun setupNotification(fileSize: Int) = coroutineScope {
        //Dismiss existing success notification
        downloadNotificationManager.dismissSuccessNotification()
        val notificationId = downloadNotificationManager.notificationId
        val notification = downloadNotificationManager.getNotification(fileSize)
        // Make service foreground to avoid process restarts during download
        val foregroundInfo = ForegroundInfo(notificationId, notification)
        setForeground(foregroundInfo)
    }

    companion object {

        const val DATA_FILE_LIST = "DATA_FILE_LIST"
        const val DATA_ANNOUNCEMENT_ID = "DATA_ANNOUNCEMENT_ID"

        fun enqueue(context: Context, announcementId: String, files: Array<String>) {
            val inputData = Data.Builder().apply {
                putString(DATA_ANNOUNCEMENT_ID, announcementId)
                putStringArray(DATA_FILE_LIST, files)
            }.build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadFileWorker>()
                .setInputData(inputData)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(context).enqueue(downloadWorkRequest)
        }
    }
}