package pt.ipca.smartcanteen.services


import pt.ipca.smartcanteen.models.RetroBar
import pt.ipca.smartcanteen.models.RetroBarStatistics
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CampusService {
    @GET("/api/v1/campus/bars")
    fun getCampusBars(@Header("Authorization") authorization: String): Call<List<RetroBar>>

    @GET("/api/v1/employee/bar/statistics")
    fun getBarStatistics(@Header("Authorization") authorization: String): Call<RetroBarStatistics>
}