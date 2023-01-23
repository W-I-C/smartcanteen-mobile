package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroFavoriteMeal(
    @SerializedName("mealid") var mealId: String,
    @SerializedName("name") var name: String,
    @SerializedName("preparationtime") var time: Int,
    @SerializedName("price") var price: Float,
    @SerializedName("description") var description: String,
    @SerializedName("canbemade") var canbemade: Boolean,
)