package pt.ipca.smartcanteen.models.retrofit.body

import com.google.gson.annotations.SerializedName

data class DirectTradeBody(
    @SerializedName("receiverEmail") val receiveremail: String,
    @SerializedName("isFree") val isfree: Boolean,
    @SerializedName("paymentMethodId") val paymentmethodid: String?,
)