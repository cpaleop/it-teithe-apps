package gr.cpaleop.upload.presentation

import android.app.Notification

interface UploadNotificationManager {

    val notificationId: Int

    val notification: Notification

    fun showSuccess()

    fun showFailure()
}