package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroNotification(
    @SerializedName("ticketid") var ticketid: String?,
    @SerializedName("notiid") var notiid: String,
    @SerializedName("receiverid") var receiverid: String,
    @SerializedName("senderid") var senderid: String,
    @SerializedName("description") var description: String,
    @SerializedName("isseen") var isseen: Boolean,
    @SerializedName("istradeproposal") var istradeproposal: Boolean,
    @SerializedName("date") var time: String,
    @SerializedName("tradeid") var tradeid: String?,
    @SerializedName("name") var name: String,
    @SerializedName("isfree") var isfree: Boolean?,
    @SerializedName("total") var total: Float?
)
