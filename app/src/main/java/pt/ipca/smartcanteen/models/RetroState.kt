package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

class RetroState(
    @SerializedName("name") var name: String,
    @SerializedName("stateid") var stateid: String,
    @SerializedName("priority") var priority: String
)