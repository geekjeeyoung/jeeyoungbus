package com.example.jeeyoungbus.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClass {

    private const val BASE_URL_YAVIN_API = "https://api.yavin.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_YAVIN_API)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val _api = retrofit.create(YavinAPI::class.java)

    val api
        get() = _api
}