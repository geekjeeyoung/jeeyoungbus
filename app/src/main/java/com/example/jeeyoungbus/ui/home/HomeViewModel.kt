package com.example.jeeyoungbus.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeeyoungbus.BusApp

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _singleJourneyTicketPrice = MutableLiveData<String>().apply {
        value = BusApp.prefs.singleJourneyTicketPrice
    }
    private val _dayTicketPrice = MutableLiveData<String>().apply {
        value = BusApp.prefs.dayTicketPrice
    }
    private val _weekTicketPrice = MutableLiveData<String>().apply {
        value = BusApp.prefs.weekTicketPrice
    }
    val singleJourneyTicketPrice: LiveData<String> = _singleJourneyTicketPrice
    val dayTicketPrice: LiveData<String> = _dayTicketPrice
    val weekTicketPrice: LiveData<String> = _weekTicketPrice
}