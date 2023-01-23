package pt.ipca.smartcanteen.models.retrofit.response

import com.google.gson.annotations.SerializedName

class RetroProfile(
    @SerializedName("name") var name: String,
    @SerializedName("campusname") var campusname: String,
    @SerializedName("barname") var barname: String,
    @SerializedName("campusid") var campusid: String,
    @SerializedName("barid") var barid: String,
    @SerializedName("email") var email: String,

    )