package gr.cpaleop.download.presentation

import android.app.Notification
import gr.cpaleop.download.domain.entities.DownloadProgress

interface DownloadNotificationManager {

    val notificationId: Int

    fun getNotification(fileSize: Int): Notification

    fun showFailure()

    fun showProgress(downloadProgress: DownloadProgress)

    fun cancelProgress()

    fun dismissSuccessNotification()

    fun showSuccess(filesSize: Int)

    fun showNoSpace(filename: String)
}