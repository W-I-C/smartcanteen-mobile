package pt.ipca.smartcanteen.services


import pt.ipca.smartcanteen.models.retrofit.response.RetroBar
import pt.ipca.smartcanteen.models.retrofit.response.RetroBarStatistics
import pt.ipca.smartcanteen.models.retrofit.response.RetroCampus
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CampusService {
    @GET("/api/v1/campus/bar/{campusId}")
    fun getCampusIdBars(@Path("campusId") campusId:String, @Header("Authorization") authorization: String): Call<List<RetroBar>>

    @GET("/api/v1/campus/bars")
    fun getCampusBars(@Header("Authorization") authorization: String): Call<List<RetroBar>>

    @GET("/api/v1/campus")
    fun getCampus(@Header("Authorization") authorization: String): Call<List<RetroCampus>>

    @GET("/api/v1/employee/bar/statistics")
    fun getBarStatistics(@Header("Authorization") authorization: String): Call<RetroBarStatistics>
}