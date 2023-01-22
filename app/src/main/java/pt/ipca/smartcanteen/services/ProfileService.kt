package pt.ipca.smartcanteen.services


import pt.ipca.smartcanteen.models.ProfileBody
import pt.ipca.smartcanteen.models.RetroProfile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT


interface ProfileService {
    @GET("/api/v1/profile")
    fun getViewProfile(@Header("Authorization") authorization: String):Call<RetroProfile>

    @PUT("/api/v1/profile")
    fun editProfile(@Body body:ProfileBody, @Header("Authorization") authorization: String):Call<RetroProfile>
}