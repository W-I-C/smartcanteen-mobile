package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroCartMeals(
    @SerializedName("name") var name: String,
    @SerializedName("price") var price: Int,
    @SerializedName("amount") var quantity: Int,
    @SerializedName("carttotal") var cartTotal: Int,
    @SerializedName("cartmealid") var cartmealId: String,
    @SerializedName("mealid") var mealId: String,
)
