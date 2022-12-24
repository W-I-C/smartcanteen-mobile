package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UndeliveredOrdersAdapterRecViewOlder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.undelivered_orders_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.undelivered_orders_card_identifier)
    val nameTv = itemView.findViewById<TextView>(R.id.undelivered_orders_card_name)
    val stateTv = itemView.findViewById<TextView>(R.id.undelivered_orders_card_state)

    fun bindData(identifierText:String, nameText:String, stateText: String){
        identifierTv.text = identifierText
        nameTv.text = nameText
        stateTv.text = stateText
    }
}