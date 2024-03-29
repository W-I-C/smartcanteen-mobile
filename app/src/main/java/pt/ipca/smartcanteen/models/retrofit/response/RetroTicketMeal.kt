package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroTicketMeal(
    @SerializedName("mealid") var mealid: String,
    @SerializedName("cartmealid") var cartmealid: String,
    @SerializedName("amount") var amount: Int,
    @SerializedName("mealprice") var mealprice: Double,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("cantakeaway") var cantakeaway: Boolean,
    @SerializedName("mealchanges") var mealchanges: List<RetroMealChange>,
)