package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class MealAllowedChangesAdapterRecViewHolder (inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.meals_allowed_changes_card, parent, false)){

    var ingNameTv = itemView.findViewById<TextView>(R.id.meals_allowed_change_card_name)
    var typeTv = itemView.findViewById<TextView>(R.id.meals_allowed_change_card_inc_dec)



    fun bindData(changeid:String,mealid:String,ingname:String, isremoveonly:Boolean,canbeincremented:Boolean,canbedecremented:Boolean){

        ingNameTv.text = ingname

        if(isremoveonly){
            typeTv.text = "Apenas remover"
        } else {
            if(canbeincremented == true && canbedecremented == true){
                typeTv.text = "Incrementar e Decrementar"
            } else if(canbeincremented == true && canbedecremented == false){
                typeTv.text = "Apenas Incrementar"
            } else if(canbeincremented == false && canbedecremented == true){
                typeTv.text = "Apenas Decrementar"
            }
        }

        // TODO: receber algo que só deixa aparecer o botão quando for para remover
    }
}