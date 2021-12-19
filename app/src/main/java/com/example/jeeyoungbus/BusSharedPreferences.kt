package com.example.jeeyoungbus

import android.content.Context
import android.content.SharedPreferences

class BusSharedPreferences(context: Context) {

    private val prefsFilename = "prefs"

    private val prefsKeySingleJourneyPrice = "singleJourneyTicketPrice"
    private val prefsKeyDayPrice = "dayTicketPrice"
    private val prefsKeyWeekPrice = "weekTicketPrice"

    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var singleJourneyTicketPrice: String?
        get() = prefs.getString(prefsKeySingleJourneyPrice, "1.10")
        set(value) = prefs.edit().putString(prefsKeySingleJourneyPrice, value).apply()

    var dayTicketPrice: String?
        get() = prefs.getString(prefsKeyDayPrice, "2.50")
        set(value) = prefs.edit().putString(prefsKeyDayPrice, value).apply()

    var weekTicketPrice: String?
        get() = prefs.getString(prefsKeyWeekPrice, "12")
        set(value) = prefs.edit().putString(prefsKeyWeekPrice, value).apply()
}