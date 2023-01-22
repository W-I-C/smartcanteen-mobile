package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

class RetroCartMeals(
    @SerializedName("name") var name:String,
    @SerializedName("price") var price:Double,
    @SerializedName("amount") var quantity:Int,
    @SerializedName("carttotal") var cartTotal:Double,
    @SerializedName("cartmealid") var cartmealId:String,
    @SerializedName("mealid") var mealId:String,
    )

{


}
