package com.example.jeeyoungbus

import android.app.Application
import java.util.prefs.Preferences

class BusApp: Application() {
    companion object {
        lateinit var prefs: BusSharedPreferences
    }

    override fun onCreate() {
        prefs = BusSharedPreferences(applicationContext)
        super.onCreate()
    }
}