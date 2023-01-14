package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class MyFavoriteMealRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_card_favorite, parent, false)){
    val nameTv = itemView.findViewById<TextView>(R.id.layout_favorite_card_title_tv)
    val timeTv = itemView.findViewById<TextView>(R.id.layout_favorite_card_time_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.layout_favorite_card_price_tv)



    fun bindData(nameText: String, quantityText: String, priceText: String){
        nameTv.text=nameText
        timeTv.text = quantityText
        priceTv.text=priceText


    }
}