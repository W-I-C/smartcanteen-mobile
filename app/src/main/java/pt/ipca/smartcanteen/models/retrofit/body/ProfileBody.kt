package pt.ipca.smartcanteen.models.retrofit.body

import com.google.gson.annotations.SerializedName

class ProfileBody(
    @SerializedName("preferredCampus") var preferredcampus: String,
    @SerializedName("preferredBar") var preferredbar: String,
)