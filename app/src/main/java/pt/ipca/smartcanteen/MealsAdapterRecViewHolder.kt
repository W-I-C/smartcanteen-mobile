package pt.ipca.smartcanteen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class MealsAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_card, parent, false)){
    val titleTv = itemView.findViewById<TextView>(R.id.layout_card_title_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.layout_card_price_tv)
    val preptimeTv = itemView.findViewById<TextView>(R.id.layout_card_time_tv)

    fun bindData(titleText:String, prepTimeText:String,priceText:String){
        titleTv.text=titleText
        preptimeTv.text = prepTimeText
        priceTv.text=priceText
    }
}