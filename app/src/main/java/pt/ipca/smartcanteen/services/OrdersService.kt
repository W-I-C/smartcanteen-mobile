package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.retrofit.response.RetroTicket
import pt.ipca.smartcanteen.models.retrofit.response.RetroTicketMeal
import retrofit2.Call
import retrofit2.http.*

interface OrdersService {
    // TODO: mudar para a rota do Henrique
    @GET("/api/v1/consumer/tickets")
    fun seeMyOrders(@Header("Authorization") authorization: String): Call<List<RetroTicket>>

    @DELETE("/api/v1/consumer/tickets/{ticketid}")
    fun removeTicket(
        @Path("ticketid") ticketid: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroTicket>>

    @GET("/api/v1/employee/tickets")
    fun seeUndeliveredOrders(@Header("Authorization") authorization: String): Call<List<RetroTicket>>


    @GET("/api/v1/ticket/{ticketid}/detail")
    fun getTicketDetails(
        @Path("ticketid") ticketid: String,
        @Header("Authorization") authorization: String
    ): Call<List<RetroTicketMeal>>

    @PUT("/api/v1/employee/tickets/{ticketid}/{stateid}")
    fun setTicketStates(
        @Path("ticketid") ticketid: String,
        @Path("stateid") stateid: String,
        @Header("Authorization") authorization: String
    ): Call<String>
}