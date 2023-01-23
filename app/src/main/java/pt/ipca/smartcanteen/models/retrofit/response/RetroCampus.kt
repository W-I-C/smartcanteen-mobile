package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroCampus(
    @SerializedName("campusid") var campusid: String,
    @SerializedName("name") var name: String,
)