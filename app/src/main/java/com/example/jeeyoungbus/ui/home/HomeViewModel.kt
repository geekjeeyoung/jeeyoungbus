package com.example.jeeyoungbus.ui.home

import android.provider.MediaStore
import androidx.lifecycle.*
import com.example.jeeyoungbus.BusApp

class HomeViewModel : ViewModel() {

    private val defaultQty = "0"

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

    val singleJourneyQty = MutableLiveData<String>().apply {
        value = defaultQty
    }

    val dayQty = MutableLiveData<String>().apply {
        value = defaultQty
    }

    val weekQty = MutableLiveData<String>().apply {
        value = defaultQty
    }

    val sumPrice: LiveData<String>
        get() = MediatorLiveData<String>().apply {
            addSource(singleJourneyQty) {
                this.value = getSumPrice(
                    it,
                    dayQty.value!!,
                    weekQty.value!!
                )
            }
            addSource(dayQty) {
                this.value = getSumPrice(
                    singleJourneyQty.value!!,
                    it,
                    weekQty.value!!
                )
            }
            addSource(weekQty) {
                this.value = getSumPrice(
                    singleJourneyQty.value!!,
                    dayQty.value!!,
                    it
                )
            }
        }

    val formFilled: LiveData<Boolean>
        get() = MediatorLiveData<Boolean>().apply {
            addSource(singleJourneyQty) {
                this.value =
                    it.isNotBlank() && !dayQty.value.isNullOrBlank() && !weekQty.value.isNullOrBlank() && !sumPrice.value.equals("0.00")
            }
            addSource(dayQty) {
                this.value =
                    it.isNotBlank() && !singleJourneyQty.value.isNullOrBlank() && !weekQty.value.isNullOrBlank() && !sumPrice.value.equals("0.00")
            }
            addSource(weekQty) {
                this.value =
                    it.isNotBlank() && !singleJourneyQty.value.isNullOrBlank() && !dayQty.value.isNullOrBlank() && !sumPrice.value.equals("0.00")
            }
            addSource(sumPrice) {
                this.value =
                    !it.equals("0.00") && !singleJourneyQty.value.isNullOrBlank() && !dayQty.value.isNullOrBlank() && !weekQty.value.isNullOrBlank()
            }
        }

    private fun getSumPrice(singleJourneyQty: String, dayQty: String, weekQty: String): String {
        val singleJourneySum: Float =
            singleJourneyTicketPrice.value!!.toFloat() * getFloatQty(singleJourneyQty)
        val daySum: Float = dayTicketPrice.value!!.toFloat() * getFloatQty(dayQty)
        val weekSum: Float = weekTicketPrice.value!!.toFloat() * getFloatQty(weekQty)
        return String.format("%.2f", (singleJourneySum + daySum + weekSum))
    }

    private fun getFloatQty(qty: String): Float {
        return if (qty.isBlank()) {
            defaultQty.toFloat()
        } else {
            qty.toFloat()
        }
    }
}