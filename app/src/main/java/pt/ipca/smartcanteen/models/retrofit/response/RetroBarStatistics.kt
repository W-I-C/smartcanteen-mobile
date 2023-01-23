package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroBarStatistics(
    @SerializedName("totalTickets") var totalTickets: Int,
    @SerializedName("deliveredTickets") var deliveredTickets: Int,
    @SerializedName("toDeliverTickets") var toDeliverTickets: Int,
    @SerializedName("tradedTickets") var tradedTickets: Int,
)