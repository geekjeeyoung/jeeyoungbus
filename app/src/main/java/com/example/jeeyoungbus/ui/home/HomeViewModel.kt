package com.example.jeeyoungbus.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jeeyoungbus.BusApp
import com.example.jeeyoungbus.network.RetrofitClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                    it.isNotBlank() && !dayQty.value.isNullOrBlank() && !weekQty.value.isNullOrBlank() && !sumPrice.value.equals(
                        "0.00"
                    )
            }
            addSource(dayQty) {
                this.value =
                    it.isNotBlank() && !singleJourneyQty.value.isNullOrBlank() && !weekQty.value.isNullOrBlank() && !sumPrice.value.equals(
                        "0.00"
                    )
            }
            addSource(weekQty) {
                this.value =
                    it.isNotBlank() && !singleJourneyQty.value.isNullOrBlank() && !dayQty.value.isNullOrBlank() && !sumPrice.value.equals(
                        "0.00"
                    )
            }
            addSource(sumPrice) {
                this.value =
                    !it.equals("0.00") && !singleJourneyQty.value.isNullOrBlank() && !dayQty.value.isNullOrBlank() && !weekQty.value.isNullOrBlank()
            }
        }

    private val _showLoadingPb = MutableLiveData<Boolean>().apply {
        value = false
    }
    val showLoadingPb: LiveData<Boolean> = _showLoadingPb

    private fun setShowLoading(boolean: Boolean) {
        _showLoadingPb.postValue(boolean)
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

    fun onSellClicked() {
        setShowLoading(true)
        val callPostNewTransaction = RetrofitClass.api.postNewTransaction()
        callPostNewTransaction.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
//                if(response.isSuccessful()) { // <--> response.code == 200
//                    // 성공 처리
//
//                    //ex)
//                    Toast.makeText(this, "${response.body().student.size}", Toast.LENGTH_SHORT).show()
//                } else { // code == 400
//                    // 실패 처리
//                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // code == 500
                // 실패 처리
            }
        })
    }
}