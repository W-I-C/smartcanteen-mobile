package pt.ipca.smartcanteen.models

import android.text.Editable
import com.google.gson.annotations.SerializedName

data class DirectTradeBody(
    @SerializedName("receiverEmail") val receiveremail: String,
    @SerializedName("isFree") val isfree: Boolean,
    @SerializedName("paymentMethodId") val paymentmethodid: String?,
)