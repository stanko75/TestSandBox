package com.example.testsandbox

import android.R.attr.action
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.jvm.java

class SeparateProcessServiceExample : Service(), CoroutineScope by MainScope() {

    private var job: Job? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val context = this
        when (intent?.action) {
            IntentGlobalActions.START_FOREGROUND_SERVICE -> {

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
                var messageNumber = 0
                job = launch {
                    while(true) {
                        messageNumber++

                        val serviceIntent = Intent(context, SeparateClassReceiverExample::class.java)
                        serviceIntent.putExtra("message", "Message number: $messageNumber")
                        sendBroadcast(serviceIntent)

                        delay(1_000)
                    }
                }

            }

            IntentGlobalActions.STOP_FOREGROUND_SERVICE -> {
                job?.cancel()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelfResult(startId)
            }
        }

        return START_STICKY
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