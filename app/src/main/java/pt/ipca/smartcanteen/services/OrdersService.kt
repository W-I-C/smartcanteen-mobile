package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.RetroTicket
import pt.ipca.smartcanteen.models.RetroTrade
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface OrdersService {
    // TODO: mudar para a rota do Henrique
    @GET("/api/v1/consumer/tickets")
    fun seeMyOrders(@Header("Authorization") authorization: String): Call<List<RetroTrade>>

    @DELETE("/api/v1/consumer/tickets/{ticketid}")
    fun removeTicket(
        @Path("ticketid") ticketid: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroTrade>>

    @GET("/api/v1/employee/tickets")
    fun seeUndeliveredOrders(@Header("Authorization") authorization: String): Call<List<RetroTrade>>

}