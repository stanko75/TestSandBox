package com.example.testsandbox

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class TestSandBoxService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        when (intent?.action) {
            IntentAction.START_FOREGROUND_SERVICE -> {

                val channelId = createNotificationChannel("my_service", "My Background Service")
                val notificationBuilder = NotificationCompat.Builder(this, channelId)
                val notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("test")
                    .setContentText("test")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(1)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build()

                startForeground(101, notification)
                val context = this

                val baseUrl: String = intent.getStringExtra("baseUrl").toString()
            }
        }

        return START_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        chan.lightColor = Color.RED
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}
