package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.LoginResponse
import pt.ipca.smartcanteen.models.RetroMeal
import pt.ipca.smartcanteen.models.RetroProfile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProfileService {
    @GET("/api/v1/profile")
    fun getViewProfile(@Header("Authorization") authorization: String):Call<RetroProfile>
}