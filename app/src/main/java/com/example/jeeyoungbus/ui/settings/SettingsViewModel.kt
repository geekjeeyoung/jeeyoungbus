package com.example.jeeyoungbus.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeeyoungbus.BusApp
import java.text.NumberFormat
import java.text.ParseException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.concurrent.timerTask
import kotlin.math.sin

class SettingsViewModel : ViewModel() {

    private val _singleJourneyTicketPrice = MutableLiveData<String>().apply {
        value = BusApp.prefs.singleJourneyTicketPrice
    }
    val singleJourneyTicketPrice: LiveData<String> = _singleJourneyTicketPrice

    private val _dayTicketPrice = MutableLiveData<String>().apply {
        value = BusApp.prefs.dayTicketPrice
    }
    val dayTicketPrice: LiveData<String> = _dayTicketPrice

    private val _weekTicketPrice = MutableLiveData<String>().apply {
        value = BusApp.prefs.weekTicketPrice
    }
    val weekTicketPrice: LiveData<String> = _weekTicketPrice

    private val _showLoadingPb = MutableLiveData<Boolean>().apply {
        value = false
    }
    val showLoadingPb: LiveData<Boolean> = _showLoadingPb

    private val _showToast = MutableLiveData<Pair<Boolean, String>>().apply {
        value = false to ""
    }
    val showToast: LiveData<Pair<Boolean, String>> = _showToast

    private fun setShowLoading(boolean: Boolean) {
        _showLoadingPb.postValue(boolean)
    }

    private fun setShowToast(pair: Pair<Boolean, String>) {
        _showToast.postValue(pair)
    }


    fun onChangeClicked(single: String, day: String, week: String) {
        val regex = Regex("[0-9]+?.[0-9]*")
        val result = arrayOf(single, day, week).map {
            if (regex.matches(it) && !it.startsWith("00")) it else null
        }
        if (result.contains(null)) {
            setShowToast(true to "Please enter the correct price")
        } else {
            setShowLoading(true)
            BusApp.prefs.apply {
                singleJourneyTicketPrice = single
                dayTicketPrice = day
                weekTicketPrice = week
            }
            Timer().schedule(timerTask {
                setShowLoading(false)
                setShowToast(true to "Successfully changed the price")
            }, 1000)
        }
    }


}