package com.dicoding.dicodingevent.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking

class DailyReminderWorker(
    appContext: Context, workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        return getEventUpcoming()
    }

    private fun getEventUpcoming(): Result {
        Log.d(TAG, "getEventUpcoming: Mulai.....")
        return try {
            val response = runBlocking {
                ApiConfig.getApiService().getClosestEvent()
            }
            val event = response.listEvents.firstOrNull()
            event?.let {
                val name = it.name
                val beginTime = it.beginTime
                showNotification(name, beginTime)
            } ?: showNotification("No upcoming events", "Failed to get the event details.")
            Log.d(TAG, "getEventUpcoming: Selesai.....")
            Result.success()
        } catch (e: Exception) {
            showNotification("Get Event Not Success", e.message)
            Log.d(TAG, "getEventUpcoming: Gagal..... : ${e.message}")
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        val channel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notification.setChannelId(CHANNEL_ID)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private val TAG = DailyReminderWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "Pengingat Event"
    }
}
