package com.example.healthx

import android.app.Application

class HealthXApplication : Application() {

    companion object{
        var instance: HealthXApplication? = null
    }

    override fun onCreate() {

        instance = this

        super.onCreate()
    }

}