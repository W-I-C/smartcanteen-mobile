package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class MenuOrdersAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_order_card, parent, false)){
    val titleTv = itemView.findViewById<TextView>(R.id.layout_card_title_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.layout_card_price_tv)
    val statusTv = itemView.findViewById<TextView>(R.id.layout_card_status_tv)
    val nOrderTv = itemView.findViewById<TextView>(R.id.layout_card_order_n_tv)


    fun bindData(titleText:String,priceText:String, statusText:String, nOrderText:String){
        titleTv.text=titleText
        statusTv.text = statusText
        priceTv.text=priceText
        nOrderTv.text=nOrderText
    }
}