package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.retrofit.body.DeviceRegisterBody
import pt.ipca.smartcanteen.models.retrofit.body.LoginBody
import pt.ipca.smartcanteen.models.retrofit.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/login")
    fun login(@Body body: LoginBody): Call<LoginResponse>


    @POST("/api/v1/device")
    fun sendDeviceToken(@Body body: DeviceRegisterBody, @Header("Authorization") authorization: String): Call<String>

    @GET("/api/v1/logout")
    fun logout(@Header("Authorization") authorization: String): Call<String>

    @GET("/api/v1/newSessionToken")
    fun getSessionToken(@Header("Authorization") authorization: String): Call<LoginResponse>
}