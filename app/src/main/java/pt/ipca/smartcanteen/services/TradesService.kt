package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.RetroPaymentMethod
import pt.ipca.smartcanteen.models.RetroTrade
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TradesService {
    @GET("/api/v1/consumer/mytrades")
    fun seeMyTrades(@Header("Authorization") authorization: String): Call<List<RetroTrade>>

    @DELETE("/api/v1/consumer/trades/{ticketid}")
    fun removeTrade(
        @Path("ticketid") ticketid: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroTrade>>

    @DELETE("/api/v1/consumer/general/trades/{generaltradeid}")
    fun removeGeneralTrade(
        @Path("generaltradeid") generaltradeid: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroTrade>>

    @GET("/api/v1/consumer/trades/available")
    fun getCampusTrades(@Header("Authorization") authorization: String): Call<List<RetroTrade>>

    @GET("/api/v1/paymentmethods")
    fun getPaymentMethods(@Header("Authorization") authorization: String): Call<List<RetroPaymentMethod>>
}