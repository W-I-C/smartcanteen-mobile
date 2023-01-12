package pt.ipca.smartcanteen.models.helpers

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

class RetroNotification(
    @SerializedName("description") var description:String,
    @SerializedName("date") var time:String,
    ) {

}
