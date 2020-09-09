package gr.cpaleop.download.presentation

import android.content.Context
import androidx.work.*
import gr.cpaleop.download.domain.usecases.DownloadFileUseCase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DownloadFileWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val downloadFilesUseCase: DownloadFileUseCase by inject()
    private val downloadNotificationManager: DownloadNotificationManager by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val files = workerParameters.inputData.getStringArray(DATA_FILE_LIST)
            ?: return@coroutineScope Result.failure()

        setupNotification(files.size)
        try {
            files.forEachIndexed { index, fileId ->
                delay(5000)
                downloadFilesUseCase(fileId)
                downloadNotificationManager.showProgress(index + 1, files.size)
            }
            downloadNotificationManager.cancelProgress()
            downloadNotificationManager.showSuccess(files.size)
            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }
    }

    private suspend fun setupNotification(fileSize: Int) = coroutineScope {
        val notificationId = downloadNotificationManager.notificationId
        val notification = downloadNotificationManager.getNotification(fileSize)
        // Make service foreground to avoid process restarts during download
        val foregroundInfo = ForegroundInfo(notificationId, notification)
        setForeground(foregroundInfo)
    }

    companion object {

        const val DATA_FILE_LIST = "DATA_FILE_LIST"

        fun enqueue(context: Context, files: Array<String>) {
            val inputData = Data.Builder().apply {
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