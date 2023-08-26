package com.example.healthx.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.healthx.R
import com.example.healthx.ui.activities.OnboardingActivity
import com.example.healthx.util.Constants.ACTION_PAUSE_SERVICE
import com.example.healthx.util.Constants.ACTION_SHOW_PEDOMETER_FRAGMENT
import com.example.healthx.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.healthx.util.Constants.ACTION_STOP_SERVICE
import com.example.healthx.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.healthx.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.healthx.util.Constants.NOTIFICATION_ID

class StepCounterService : LifecycleService() {

    var isFirstWalk = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstWalk) {
                        startForegroundService()
                        isFirstWalk = false
                    } else Toast.makeText(this, "Resuming Service...", Toast.LENGTH_SHORT).show()
                }

                ACTION_PAUSE_SERVICE -> {

                }

                ACTION_STOP_SERVICE -> {

                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.round_directions_walk_24)
            .setContentTitle("Step Counting")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, OnboardingActivity::class.java).also {
            it.action = ACTION_SHOW_PEDOMETER_FRAGMENT
        },
        FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}