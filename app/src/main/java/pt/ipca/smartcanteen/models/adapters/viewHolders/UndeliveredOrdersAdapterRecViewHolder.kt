package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class UndeliveredOrdersAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.undelivered_orders_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.undelivered_orders_card_identifier)
    val nameTv = itemView.findViewById<TextView>(R.id.undelivered_orders_card_name)
    val stateTv = itemView.findViewById<TextView>(R.id.undelivered_orders_card_state)

    fun bindData(identifierText:Int, nameText:String, stateText: String){
        identifierTv.text = identifierText.toString()
        nameTv.text = nameText
        stateTv.text = stateText

        if(stateText == "Não Iniciado" || stateText == "Atraso"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.redLogout))
        } else if(stateText == "Pronto"){
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.background_color))
        } else if(stateText == "Em Preparação") {
            stateTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
        }
    }
}