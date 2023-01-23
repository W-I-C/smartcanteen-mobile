package pt.ipca.smartcanteen.models.retrofit.body

import com.google.gson.annotations.SerializedName

data class GeneralTradeBody(
    @SerializedName("isFree") val isfree: Boolean,
    @SerializedName("paymentMethodId") val paymentmethodid: String?,
)