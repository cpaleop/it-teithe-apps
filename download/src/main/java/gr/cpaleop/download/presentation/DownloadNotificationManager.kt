package gr.cpaleop.download.presentation

import android.app.Notification

interface DownloadNotificationManager {

    val notificationId: Int

    fun getNotification(fileSize: Int): Notification

    fun showFailure()

    fun showProgress(current: Int, total: Int)

    fun cancelProgress()

    fun showSuccess(filesSize: Int)

    fun showNoSpace(filename: String)
}