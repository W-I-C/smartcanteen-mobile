package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R

class BarMenuMealsAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.bar_menu_meal_card, parent, false)){
    val titleTv = itemView.findViewById<TextView>(R.id.bar_menu_card_title_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.bar_menu_card_price_tv)
    val preptimeTv = itemView.findViewById<TextView>(R.id.bar_menu_card_time_tv)

    fun bindData(titleText:String, prepTimeText:String,priceText:String){
        titleTv.text=titleText
        preptimeTv.text = prepTimeText
        priceTv.text=priceText
    }
}