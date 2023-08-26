package com.example.healthx.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
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

    companion object {
        val stepCountLiveData = MutableLiveData<Float>()
    }

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

        countSteps()

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

    private fun countSteps() {

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepCounter != null) {

            val stepCounterListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                            val stepCount = event.values[0]
                            stepCountLiveData.postValue(stepCount)
                        } else Toast.makeText(
                            this@StepCounterService,
                            "Something Went Wrong",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

                }
            }

            sensorManager.registerListener(
                stepCounterListener,
                stepCounter,
                SensorManager.SENSOR_DELAY_FASTEST
            )

        } else {
            Toast.makeText(this, "Sensor not available", Toast.LENGTH_SHORT).show()
            stopSelf()
        }

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