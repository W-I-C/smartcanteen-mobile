package pt.ipca.smartcanteen

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface NewSessionTokenService {
    // TODO: mudar para a rota do Henrique
    @GET("/api/v1/newSessionToken")
    fun getSessionToken(@Header("Authorization") authorization: String): Call<LoginResponse>
}