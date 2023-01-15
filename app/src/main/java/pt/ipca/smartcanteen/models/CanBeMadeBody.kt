package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

data class CanBeMadeBody(
    @SerializedName("status") val status: Boolean
)