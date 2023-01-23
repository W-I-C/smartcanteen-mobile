package pt.ipca.smartcanteen.models.helpers

import com.google.gson.annotations.SerializedName

class RetroNotification(
    @SerializedName("description") var description: String,
    @SerializedName("date") var time: String,
)
