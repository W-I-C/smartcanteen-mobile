package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyOrdersCartRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.activity_cart_recycle, parent, false)){
    val nameTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_name)
    val quantityTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_quantity)
    val priceTv = itemView.findViewById<TextView>(R.id.my_orders_cart_card_price)

    fun bindData(nameText:String, quantityText: String, priceText:String){
        nameTv.text=nameText
        quantityTv.text = quantityText
        priceTv.text=priceText

    }
}