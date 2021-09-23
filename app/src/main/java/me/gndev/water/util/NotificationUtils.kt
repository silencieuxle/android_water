package me.gndev.water.util

import android.app.PendingIntent
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtils {
    fun sendNotification(
        context: Context,
        channelId: String,
        title: String,
        contentText: String,
        @DrawableRes icon: Int,
        pendingIntent: PendingIntent? = null,
        notificationId: Int? = null
    ) {
        val builder =
            NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(
                notificationId ?: DateTimeUtils.getCurrentTimeAsNumber().toInt(),
                builder.build()
            )
        }
    }
}