package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

class ProfileBody(
    @SerializedName("preferredcampus") var preferredcampus:String,
    @SerializedName("preferredbar") var preferredbar:String,
)