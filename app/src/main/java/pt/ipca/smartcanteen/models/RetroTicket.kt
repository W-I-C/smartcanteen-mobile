package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class RetroTicket (
    @SerializedName("barname") var barname: String,
    @SerializedName("ticketid") var ticketid: String,
    @SerializedName("ownername") var ownername: String,
    @SerializedName("statename") var statename: String,
    @SerializedName("cartid") var cartid: String,
    @SerializedName("emissiondate") var emissiondate: String,
    @SerializedName("pickuptime") var pickuptime: String,
    @SerializedName("ticketamount") var ticketamount: Int,
    @SerializedName("total") var total: Float,
    @SerializedName("nencomenda") var norder: Int,
    @SerializedName("isfree") var isfree: Boolean,
    @SerializedName("ticketmeals") var ticketmeals: List<RetroTicketMeal>
    )
