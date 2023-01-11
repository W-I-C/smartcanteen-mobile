package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrdersAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.my_order_card, parent, false)){
    val identifierTv = itemView.findViewById<TextView>(R.id.my_orders_card_identifier)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_card_price)
    val stateTv = itemView.findViewById<TextView>(R.id.my_orders_card_state)

    val buttonTrade = itemView.findViewById<Button>(R.id.my_orders_card_button_trade)

    fun setOnClickListener(listener: (View) -> Unit) {
        buttonTrade.setOnClickListener(listener)
    }

    fun bindData(identifierText: Int, quantityText: Int, priceText: Int, stateText: String){
        identifierTv.text= identifierText.toString()
        quantityTv.text = quantityText.toString()
        priceTv.text= priceText.toString()
        stateTv.text=stateText
    }
}