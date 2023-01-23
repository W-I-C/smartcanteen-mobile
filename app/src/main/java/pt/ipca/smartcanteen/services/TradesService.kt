package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.retrofit.body.DirectTradeBody
import pt.ipca.smartcanteen.models.retrofit.body.DirectTradePaymentBody
import pt.ipca.smartcanteen.models.retrofit.body.GeneralTradeBody
import pt.ipca.smartcanteen.models.retrofit.response.RetroPaymentMethod
import pt.ipca.smartcanteen.models.retrofit.response.RetroTrade
import pt.ipca.smartcanteen.models.retrofit.response.RetroTradePayment
import retrofit2.Call
import retrofit2.http.*

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

    @PUT("/api/v1/consumer/trades/{ticketId}/direct")
    fun directTicketTrade(
        @Path("ticketId") ticketId: String,
        @Header("Authorization") authorization: String,
        @Body body: DirectTradeBody
    ): Call<String>

    @PUT("/api/v1/consumer/trades/{ticketId}/general")
    fun generalTicketTrade(
        @Path("ticketId") ticketid: String,
        @Header("Authorization") authorization: String,
        @Body body: GeneralTradeBody
    ): Call<String>

    @GET("/api/v1/consumer/general/trade/{generalTradeId}")
    fun getTradePaymentMethod(
        @Path("generalTradeId") generaltradeid: String,
        @Header("Authorization") authorization: String
    ): Call<RetroTradePayment>

    @GET("/api/v1/consumer/general/trades/{generalTradeId}")
    fun acceptGeneralTrade(
        @Path("generalTradeId") generaltradetd: String,
        @Header("Authorization") authorization: String
    ): Call<String>

    @GET("/api/v1/consumer/direct/trade/{tradeId}")
    fun getDirectTradePaymentMethod(
        @Path("tradeId") tradeid: String,
        @Header("Authorization") authorization: String
    ): Call<RetroTradePayment>

    @PUT("/api/v1/consumer/trades/{ticketId}")
    fun acceptDirectTrade(
        @Path("ticketId") ticketId: String,
        @Header("Authorization") authorization: String,
        @Body body: DirectTradePaymentBody
    ): Call<String>
}