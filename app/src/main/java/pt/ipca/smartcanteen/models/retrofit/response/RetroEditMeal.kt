package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroEditMeal(
    @SerializedName("mealid") var mealid: String,
    @SerializedName("name") var name: String,
    @SerializedName("preparationTime") var time: Int,
    @SerializedName("description") var description: String,
    @SerializedName("canTakeAway") var cantakeaway: Boolean,
    @SerializedName("price") var price: Float,

    )