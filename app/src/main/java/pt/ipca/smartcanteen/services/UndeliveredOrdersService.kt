package pt.ipca.smartcanteen

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface UndeliveredOrdersService {
    @GET("/api/v1/employee/tickets")
    fun seeUndeliveredOrders(@Header("Authorization") authorization: String): Call<List<RetroTicket>>
}