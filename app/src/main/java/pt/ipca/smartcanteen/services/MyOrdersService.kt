package pt.ipca.smartcanteen

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface MyOrdersService {
    // TODO: mudar para a rota do Henrique
    @GET("/api/v1/consumer/tickets")
    fun seeMyOrders(@Header("Authorization") authorization: String): Call<List<RetroTrade>>
}