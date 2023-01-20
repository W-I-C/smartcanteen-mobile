package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class RetroTrade (
    @SerializedName("ticketid") val ticketid: String,
    @SerializedName("generaltradeid") val generaltradeid: String?,
    @SerializedName("barid") val barid: String?,
    @SerializedName("nencomenda") val norder: Int,
    @SerializedName("ticketamount") val ticketamount: Int,
    @SerializedName("total") val total: Float,
    @SerializedName("statename") val statename: String,
    @SerializedName("ownername") val ownername: String,
    @SerializedName("receivername") val receivername: String?,
    @SerializedName("receptordecision") val receptordecision: Int?,
    @SerializedName("cartid") val cartid: String,
    @SerializedName("emissiondate") val emissiondate: String,
    @SerializedName("pickuptime") val pickuptime: String,
    @SerializedName("isfree") val isfree: Boolean,
    @SerializedName("paymentmethod") val paymentmethod: Boolean,
    @SerializedName("isgeneraltrade") val isgeneraltrade: Boolean,
    @SerializedName("ticketmeals") var ticketmeals: List<RetroTicketMeal>
) {}