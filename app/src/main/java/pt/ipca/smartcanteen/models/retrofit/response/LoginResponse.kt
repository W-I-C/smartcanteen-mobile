package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("role") val role: String,
    @SerializedName("uid") val uid: String
)