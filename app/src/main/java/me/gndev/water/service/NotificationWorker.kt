package me.gndev.water.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.gndev.water.MainActivity
import me.gndev.water.R
import me.gndev.water.util.NotificationUtils

class NotificationWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val intent = Intent(appContext, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0)

        NotificationUtils.sendNotification(
            appContext,
            appContext.getString(R.string.app_name),
            "TEST",
            "DRINK NOW",
            R.drawable.ic_water_drop,
            pendingIntent)
        return Result.success()
    }
}