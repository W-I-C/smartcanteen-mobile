package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class MealAllowedChangesAdapterRecViewHolder (inflater: LayoutInflater, val parent: ViewGroup,
                                              private var activity: Activity):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.meals_allowed_changes_card, parent, false)){

    var ingNameTv = itemView.findViewById<TextView>(R.id.meals_allowed_change_card_name)
    var typeTv = itemView.findViewById<TextView>(R.id.meals_allowed_change_card_inc_dec)

    fun bindData(changeid:String,mealid:String,ingname:String, isremoveonly:Boolean,canbeincremented:Boolean,canbedecremented:Boolean){

        ingNameTv.text = ingname

        if(isremoveonly){
            typeTv.text = activity.getString(R.string.only_remove)
        } else {
            if(canbeincremented == true && canbedecremented == true){
                typeTv.text = activity.getString(R.string.increment_decrement)
            } else if(canbeincremented == true && canbedecremented == false){
                typeTv.text = activity.getString(R.string.only_increment)
            } else if(canbeincremented == false && canbedecremented == true){
                activity.getString(R.string.only_decrement)
            }
        }
    }
}