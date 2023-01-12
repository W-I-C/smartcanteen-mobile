package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class RetroTrade (
    @SerializedName("ticketid") val ticketid: String,
    @SerializedName("nencomenda") val norder: Int,
    @SerializedName("ticketamount") val ticketamount: Int,
    @SerializedName("total") val total: Float,
    @SerializedName("statename") val statename: String,
    @SerializedName("barname") val barname: String,
    @SerializedName("ownername") val ownername: String,
    @SerializedName("cartid") val cartid: String,
    @SerializedName("emissiondate") val emissiondate: String,
    @SerializedName("pickuptime") val pickuptime: String,
    @SerializedName("isfree") val isfree: Boolean,
    @SerializedName("isgeneraltrade") val isgeneraltrade: Boolean,
    @SerializedName("generaltradeid") val generaltradeid: String
) {}