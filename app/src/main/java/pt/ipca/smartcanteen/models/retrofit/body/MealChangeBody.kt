package pt.ipca.smartcanteen.models.retrofit.body

import com.google.gson.annotations.SerializedName

data class MealChangeBody(
    @SerializedName("ingname") val ingname: String,
    @SerializedName("ingdosage") val ingdosage: String,
    @SerializedName("isremoveonly") val isremoveonly: Boolean,
    @SerializedName("canbeincremented") val canbeincremented: Boolean,
    @SerializedName("canbedecremented") val canbecremented: Boolean,
    @SerializedName("incrementlimit") val incrementlimit: Int?,
    @SerializedName("decrementlimit") val decrementlimit: Int?,
    @SerializedName("defaultvalue") val defaultValue: Int
)