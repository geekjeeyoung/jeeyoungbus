package com.example.jeeyoungbus.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface YavinAPI {
    @GET("api/transactions")
    fun extractAllTransctions() : Call<Any>

    @POST("api/v1/pos/payment/new/")
    fun postNewTransaction() : Call<Any>

    @POST("api/v1/pos/notification/new/")
    fun sendPushNotification()
}