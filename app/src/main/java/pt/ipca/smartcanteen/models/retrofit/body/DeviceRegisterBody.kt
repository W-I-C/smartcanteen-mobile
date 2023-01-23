package pt.ipca.smartcanteen.models.retrofit.body

import com.google.gson.annotations.SerializedName

class DeviceRegisterBody(
    @SerializedName("token") val token: String,
)