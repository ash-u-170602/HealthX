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
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthx.R
import com.example.healthx.db.DatabaseViewModel
import com.example.healthx.ui.activities.OnboardingActivity
import com.example.healthx.util.Constants.ACTION_PAUSE_SERVICE
import com.example.healthx.util.Constants.ACTION_SHOW_PEDOMETER_FRAGMENT
import com.example.healthx.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.healthx.util.Constants.ACTION_STOP_SERVICE
import com.example.healthx.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.healthx.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.healthx.util.Constants.NOTIFICATION_ID
import com.example.healthx.util.todayDate
import kotlin.math.sqrt

class StepCounterService : LifecycleService() {

    private var sensorManager: SensorManager? = null
    private var stepCounterListener: SensorEventListener? = null

    private var magnitudePrevious = 0.toDouble()

    private var startTime: Long = 55
    private var isTimerRunning = false

    companion object {
        var totalSteps = 0f
        val stepCountLiveData = MutableLiveData<Float>()
        val elapsedTimeLiveData = MutableLiveData<String>()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show()
                    startTimer()
                    startForegroundService()
                }

                ACTION_PAUSE_SERVICE -> {
                    Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show()
                }

                ACTION_STOP_SERVICE -> {
                    Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
                    stopForeground(true)
                    stopCount()
                    isTimerRunning = false
                    stopSelf()
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun startForegroundService() {

        countSteps()
        startTime = System.currentTimeMillis()

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
            .setContentText("Tap to see progress")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopCount() {
        sensorManager?.unregisterListener(stepCounterListener)
        stepCounterListener = null
        Toast.makeText(this, totalSteps.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun countSteps() {


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounter = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (stepCounter != null) {

            stepCounterListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                            val xAcceleration = event.values[0]
                            val yAcceleration = event.values[1]
                            val zAcceleration = event.values[2]

                            val calculator =
                                (xAcceleration * xAcceleration + yAcceleration * yAcceleration + zAcceleration * zAcceleration).toDouble()

                            val magnitude = sqrt(calculator)
                            val magnitudeDelta = magnitude - magnitudePrevious
                            magnitudePrevious = magnitude

                            if (magnitudeDelta >= 10) {
                                stepCountLiveData.postValue(totalSteps++)
                            }

                        } else {
                            Toast.makeText(
                                this@StepCounterService,
                                "Something Went Wrong",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            stopSelf()
                        }
                    }

                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

                }
            }
            sensorManager?.registerListener(
                stepCounterListener,
                stepCounter,
                SensorManager.SENSOR_DELAY_NORMAL
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


    private fun startTimer() {
        isTimerRunning = true

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                if (isTimerRunning) {
                    val elapsedTime = System.currentTimeMillis() - startTime
                    val formattedTime = formatElapsedTime(elapsedTime)
                    elapsedTimeLiveData.postValue(formattedTime)
                    handler.postDelayed(this, 1000) // Update every second
                }
            }
        })
    }

    private fun formatElapsedTime(elapsedTimeMillis: Long): String {
        val seconds = (elapsedTimeMillis / 1000).toInt() % 60
        val minutes = (elapsedTimeMillis / (1000 * 60) % 60).toInt()
        val hours = (elapsedTimeMillis / (1000 * 60 * 60)).toInt()

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


}