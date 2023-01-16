package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class MealBody(
    @SerializedName("name") var name: String,
    @SerializedName("preparationTime") var time: Int,
    @SerializedName("description") var description: String,
    @SerializedName("canTakeAway") var cantakeaway: Boolean,
    @SerializedName("price") var price: Float
)