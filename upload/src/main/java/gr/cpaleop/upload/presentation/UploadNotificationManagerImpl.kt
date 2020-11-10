package gr.cpaleop.upload.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import gr.cpaleop.upload.R

class UploadNotificationManagerImpl(
    private val context: Context
) : UploadNotificationManager {

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
        get() = UPLOAD_ID

    override val notification: Notification
        get() {
            notificationManager.cancel(MESSAGE_ID)
            return NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_upload)
                .setContentTitle(
                    context.getString(
                        R.string.create_announcement_upload_notification_title
                    )
                )
                .setProgress(0, 0, true)
                .setOngoing(true)
                .build()
        }

    override fun showSuccess() {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_done)
            .setContentTitle(context.getString(R.string.create_announcement_upload_notification_title))
            .setContentText(context.getString(R.string.create_announcement_upload_notification_text_success))
            .setAutoCancel(true)

        notificationManager.notify(MESSAGE_ID, builder.build())
    }

    override fun showFailure() {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_error)
            .setContentTitle(context.getString(R.string.create_announcement_upload_notification_title))
            .setContentText(context.getString(R.string.create_announcement_upload_notification_text_failure))
            .setAutoCancel(true)

        notificationManager.notify(MESSAGE_ID, builder.build())
    }

    companion object {

        private const val UPLOAD_ID = 12
        private const val MESSAGE_ID = 13
        private const val CHANNEL_ID = "upload_primary_channel"
        private const val CHANNEL_NAME = "Upload Primary Channel"
    }
}