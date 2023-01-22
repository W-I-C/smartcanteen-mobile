package pt.ipca.smartcanteen.models

import com.google.gson.annotations.SerializedName

class RetroProfile (
    @SerializedName("name") var name:String,
    @SerializedName("campusname") var campusname:String,
    @SerializedName("barname") var barname:String,

   )
{

}