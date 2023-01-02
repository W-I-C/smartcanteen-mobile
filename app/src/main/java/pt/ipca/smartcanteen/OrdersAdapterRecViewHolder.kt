package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.my_order_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.my_orders_card_identifier)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_card_price)
    val stateTv = itemView.findViewById<TextView>(R.id.my_orders_card_state)

    fun bindData(identifierText:String, quantityText: String, priceText:String, stateText: String){
        identifierTv.text=identifierText
        quantityTv.text = quantityText
        priceTv.text=priceText
        stateTv.text=stateText
    }
}