package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

class RetroMeal(
    @SerializedName("mealid") var mealid:String,
    @SerializedName("barid") var barid:String,
    @SerializedName("name") var name:String,
    @SerializedName("preparationtime") var preparationtime:String,
    @SerializedName("description") var description:String,
    @SerializedName("cantakeaway") var cantakeaway:Boolean,
    @SerializedName("price") var price:String,
    @SerializedName("canbemade") var canbemade:Boolean,

)