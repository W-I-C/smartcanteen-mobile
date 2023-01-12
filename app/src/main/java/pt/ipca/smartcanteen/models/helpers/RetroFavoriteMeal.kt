package pt.ipca.smartcanteen.models.helpers

import com.google.gson.annotations.SerializedName

class RetroFavoriteMeal(
    @SerializedName("name") var name:String,
    @SerializedName("preparationtime") var time:Int,
    @SerializedName("price") var price:Float,
    @SerializedName("url") var url:String,) {
}