package gr.cpaleop.download.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import gr.cpaleop.download.R

class DownloadNotificationManagerImpl(
    private val context: Context
) : DownloadNotificationManager {

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val primaryChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(
                    null, AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(primaryChannel)
        }
    }

    override val notificationId: Int
        get() = DOWNLOAD_ID

    override fun getNotification(fileSize: Int): Notification {
        /*val title = filenames[0]*/
        val title = context.getString(R.string.download_notification_title)
        val text = context.getString(R.string.download_notification_text, 1, fileSize)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setProgress(0, 0, true)
            .setOngoing(true)
            .build()
    }

    override fun showFailure() {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_notification)
            .setContentTitle(context.getString(R.string.download_notification_title))
            .setContentText(context.getString(R.string.download_notification_error))
            .setAutoCancel(true)

        notificationManager.notify(MESSAGE_ID, builder.build())
    }

    override fun showProgress(current: Int, total: Int) {
        val title = context.getString(R.string.download_notification_title)
        val text = context.getString(R.string.download_notification_text, current, total)
        val percent = ((current.toFloat() / total.toFloat()) * 100).toInt()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setProgress(100, percent, false)
            .setOngoing(true)
        /*.addAction(R.drawable.ic_cancel, cancelText, pendingIntent)*/

        notificationManager.notify(DOWNLOAD_ID, builder.build())
    }

    override fun cancelProgress() {
        notificationManager.cancel(DOWNLOAD_ID)
    }

    override fun dismissSuccessNotification() {
        notificationManager.cancel(MESSAGE_ID)
    }

    // TODO: Show documents fragment on click
    override fun showSuccess(filesSize: Int) {
        val title = context.getString(R.string.download_notification_success_title, filesSize)
        val text = context.getString(R.string.download_notification_success_tap_to_navigate)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_done)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)

        notificationManager.notify(MESSAGE_ID, builder.build())
    }

    override fun showNoSpace(filename: String) {
        val retry = context.getString(R.string.download_notification_retry)
        val intent = Intent()
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download_notification)
            .setContentTitle(context.getString(R.string.download_notification_filename, filename))
            .setContentText(context.getString(R.string.download_notification_no_space))
            .setAutoCancel(true)
            .addAction(R.drawable.ic_cancel, retry, pendingIntent)

        notificationManager.notify(MESSAGE_ID, builder.build())
    }

    companion object {

        @VisibleForTesting
        const val DOWNLOAD_ID = 10

        @VisibleForTesting
        const val MESSAGE_ID = 2 // TODO: Same as ScanNotificationManager
        private const val CHANNEL_ID = "primary_channel"
        private const val CHANNEL_NAME = "Primary Channel"
        const val ACTION_SHOW_DOCUMENTS = "gr.cpaleop.download.ACTION_SHOW_DOCUMENTS"
    }
}