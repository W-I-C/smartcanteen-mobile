package pt.ipca.smartcanteen.services


import pt.ipca.smartcanteen.models.RetroTrade
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CampusTradesService {
    @GET("/api/v1/consumer/trades/available")
    fun getCampusTrades(@Header("Authorization") authorization: String): Call<List<RetroTrade>>
}