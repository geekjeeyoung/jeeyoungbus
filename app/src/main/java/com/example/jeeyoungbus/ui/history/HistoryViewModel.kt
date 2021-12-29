package com.example.jeeyoungbus.ui.history

import androidx.lifecycle.ViewModel
import com.example.jeeyoungbus.network.RetrofitClass
import retrofit2.*

class HistoryViewModel : ViewModel() {

    fun getTransactionHistory() {

//        setShowLoading(true)
        val callPostNewTransaction = RetrofitClass.api.extractAllTransctions()
        callPostNewTransaction.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    // response.code == 200
                    // Log.i("success", response.body().toString())

                } else { // code == 400
                    // failure
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // code == 500

            }
        })
    }
}
