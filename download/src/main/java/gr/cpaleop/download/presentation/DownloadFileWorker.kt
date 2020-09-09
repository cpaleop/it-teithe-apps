package gr.cpaleop.download.presentation

import android.content.Context
import androidx.work.*
import gr.cpaleop.download.domain.usecases.DownloadFileUseCase
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class DownloadFileWorker(
    context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val downloadFilesUseCase: DownloadFileUseCase by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val fileId =
            workerParameters.inputData.getString(FILE_ID) ?: return@coroutineScope Result.failure()

        try {
            downloadFilesUseCase(fileId)
            Result.success()
        } catch (t: Throwable) {
            Timber.e(t)
            Result.failure()
        }
    }

    companion object {

        const val FILE_ID = "FILE_ID"

        fun enqueue(context: Context, fileId: String) {
            val inputData = Data.Builder().apply {
                putString(FILE_ID, fileId)
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