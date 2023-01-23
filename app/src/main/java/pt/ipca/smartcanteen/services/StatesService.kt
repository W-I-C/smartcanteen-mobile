package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.retrofit.response.RetroState
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface StatesService {
    @GET("/api/v1/states")
    fun getStates(@Header("Authorization") authorization: String): Call<List<RetroState>>
}