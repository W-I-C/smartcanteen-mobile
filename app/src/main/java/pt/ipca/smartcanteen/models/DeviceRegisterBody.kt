package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

class DeviceRegisterBody(
    @SerializedName("token") val token: String,
)