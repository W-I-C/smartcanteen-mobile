package pt.ipca.smartcanteen

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/api/v1/login")
    fun login(@Body body: LoginBody): Call<LoginResponse>
}