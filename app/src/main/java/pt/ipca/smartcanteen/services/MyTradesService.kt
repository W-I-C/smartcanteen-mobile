package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.RetroTrade
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MyTradesService {
    // TODO: mudar para a rota do Henrique
    @GET("/api/v1/consumer/mytrades")
    fun seeMyTrades(@Header("Authorization") authorization: String): Call<List<RetroTrade>>

    @DELETE("/api/v1/consumer/trades/{ticketid}")
    fun removeTrade(
        @Path("ticketid") ticketid: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroTrade>>
}