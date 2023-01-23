package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroMealChange(
    @SerializedName("cartmealchangeid") var cartmealchangeid: String,
    @SerializedName("cartmealid") var cartmealid: String,
    @SerializedName("ingname") var ingname: String,
    @SerializedName("ingamount") var ingamount: Int,
    @SerializedName("isremoveonly") var isremoveonly: Boolean,
    @SerializedName("canbeincremented") var canbeincremented: Boolean,
    @SerializedName("canbedecremented") var canbedecremented: Boolean
)