package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class RetroTicket (
    @SerializedName("ticketid") val ticketid: String,
    @SerializedName("nencomenda") val nencomenda: Int,
    @SerializedName("name") val name: String,
    @SerializedName("statename") val statename: String,) {}
