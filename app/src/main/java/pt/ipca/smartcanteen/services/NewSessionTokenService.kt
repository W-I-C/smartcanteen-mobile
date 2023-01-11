package pt.ipca.smartcanteen.services

import pt.ipca.smartcanteen.models.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface NewSessionTokenService {
    // TODO: mudar para a rota do Henrique
    @GET("/api/v1/newSessionToken")
    fun getSessionToken(@Header("Authorization") authorization: String): Call<LoginResponse>
}