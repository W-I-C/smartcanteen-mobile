package pt.ipca.smartcanteen.models.retrofit.body

import com.google.gson.annotations.SerializedName

data class DirectTradePaymentBody(
    @SerializedName("receptorDecision") val receptorDecision: Int,
)