package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class RetroPaymentMethod(
    @SerializedName("methodid") var mealid: String,
    @SerializedName("name") var name: String
)