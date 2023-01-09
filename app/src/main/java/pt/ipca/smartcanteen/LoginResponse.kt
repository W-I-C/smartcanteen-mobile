package pt.ipca.smartcanteen

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("token") val token: String,
    @SerializedName("role") val role: String
)