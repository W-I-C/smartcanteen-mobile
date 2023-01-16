package pt.ipca.smartcanteen.models.helpers

import com.google.gson.annotations.SerializedName

class RetroCreateMeal(
@SerializedName("name") var name:String,
@SerializedName("preparationtime") var time:String,
@SerializedName("description") var description:String,
@SerializedName("price") var price:Double,
@SerializedName("allowedchanges") var allowedChanges: List<AllowedChange>
)

class AllowedChange(
    @SerializedName("ingname") var ingName: String,
    @SerializedName("ingdosage") var ingDosage: String,
    @SerializedName("isremoveonly") var isRemoveOnly: Boolean,
    @SerializedName("canbeincremented") var canBeIncremented: Boolean,
    @SerializedName("canbedecremented") var canBeDecremented: Boolean,
    @SerializedName("incrementlimit") var incrementLimit: Int,
    @SerializedName("decrementlimit") var decrementLimit: Int
)

 {
}