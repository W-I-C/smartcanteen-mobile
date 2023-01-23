package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroMeal(
    @SerializedName("mealid") var mealid: String,
    @SerializedName("barid") var barid: String,
    @SerializedName("name") var name: String,
    @SerializedName("preparationtime") var preparationtime: Int,
    @SerializedName("description") var description: String,
    @SerializedName("cantakeaway") var cantakeaway: Boolean,
    @SerializedName("price") var price: Float,
    @SerializedName("canbemade") var canbemade: Boolean,
    @SerializedName("isfavorite") var isfavorite: Boolean,
    )