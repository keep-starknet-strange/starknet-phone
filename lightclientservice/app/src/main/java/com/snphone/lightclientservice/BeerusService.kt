package com.snphone.lightclientservice

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.widget.Toast
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat


class BeerusService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var isForeground = false

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            startForeground(NOTIFICATION_ID, createNotification("Running BeerusClient..."))
            isForeground = true

            try {
                val dataDir = "data/data/com.snphone.lightclientservice"
                val runResponse = BeerusClient.run(
                    BuildConfig.ETH_SEPOLIA_RPC_URL,
                    BuildConfig.STARKNET_SEPOLIA_RPC_URL,
                    dataDir
                )
                Log.d(TAG, "BeerusClient.run completed: $runResponse")
            } catch (e: Exception) {
                Log.e(TAG, "Error in BeerusClient.run", e)
            } finally {
                if (isForeground) {
                    stopForeground(true)
                    isForeground = false
                }
            }
        }
    }


    override fun onCreate() {
        // Starting up the thread that runs the service. Note that this creates
        // a seperate thread because the service normally runs in the process's
        // main thread, which we dont want to block. We also make it background
        // priority so CPU-intensive work wil not disrupt our UI.
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // getting the HandlerThread's Looper and using it for out Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start id so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // if we get killed, after returning from here, restart
        return START_STICKY
    }

    private fun createNotification(message: String): Notification {
        val channelId = "BeerusServiceChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Beerus Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Beerus Service")
            .setContentText(message)
            .setSmallIcon(R.drawable.notification_icon)
            .build()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "BeerusService"
        private const val NOTIFICATION_ID = 1
    }
}