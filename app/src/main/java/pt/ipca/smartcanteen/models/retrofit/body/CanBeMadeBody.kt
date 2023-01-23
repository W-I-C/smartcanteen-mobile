package pt.ipca.smartcanteen.models.retrofit.body

import com.google.gson.annotations.SerializedName

data class CanBeMadeBody(
    @SerializedName("status") val status: Boolean
)