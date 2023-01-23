package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroAllowedChanges(
    @SerializedName("changeid") var changeid: String,
    @SerializedName("mealid") var mealid: String,
    @SerializedName("ingname") var ingname: String,
    @SerializedName("ingdosage") var ingdosage: String,
    @SerializedName("isremoveonly") var isremoveonly: Boolean,
    @SerializedName("canbeincremented") var canbeincremented: Boolean,
    @SerializedName("canbedecremented") var canbedecremented: Boolean,
    @SerializedName("incrementlimit") var incrementlimit: Int?,
    @SerializedName("decrementlimit") var decrementlimit: Int?,
    @SerializedName("defaultvalue") var default: Int,
)