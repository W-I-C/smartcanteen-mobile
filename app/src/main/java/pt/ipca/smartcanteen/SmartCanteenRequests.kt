package pt.ipca.smartcanteen

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SmartCanteenRequests() {
    val BASE_URL = "https://smartcanteen-api.herokuapp.com"
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}