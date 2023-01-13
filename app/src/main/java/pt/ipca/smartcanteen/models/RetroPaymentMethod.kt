package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class RetroPaymentMethod(
    @SerializedName("methodid") var methodid: String,
    @SerializedName("name") var name: String
)