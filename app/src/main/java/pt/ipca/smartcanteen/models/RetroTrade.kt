package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class RetroTrade (
    @SerializedName("ticketid") val ticketid: String,
    @SerializedName("nencomenda") val nencomenda: Int,
    @SerializedName("ticketamount") val ticketamount: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("name") val statename: String) {}