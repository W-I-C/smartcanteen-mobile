package pt.ipca.smartcanteen.services


import pt.ipca.smartcanteen.models.RetroBar
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CampusService {
    @GET("/api/v1/campus/bars")
    fun getCampusBars(@Header("Authorization") authorization: String): Call<List<RetroBar>>
}