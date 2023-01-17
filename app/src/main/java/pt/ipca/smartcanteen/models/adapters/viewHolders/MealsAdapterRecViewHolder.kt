package pt.ipca.smartcanteen.models.adapters.viewHolders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.ipca.smartcanteen.R
import pt.ipca.smartcanteen.models.helpers.ImagesHelper


class MealsAdapterRecViewHolder(inflater: LayoutInflater, val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.layout_card, parent, false)) {
    val titleTv = itemView.findViewById<TextView>(R.id.layout_card_title_tv)
    val priceTv = itemView.findViewById<TextView>(R.id.layout_card_price_tv)
    val preptimeTv = itemView.findViewById<TextView>(R.id.layout_card_time_tv)
    val cardImage = itemView.findViewById<ImageView>(R.id.layout_card_iv)

    fun bindData(titleText: String, prepTimeText: String, priceText: String, url:String) {
        titleTv.text = titleText
        preptimeTv.text = prepTimeText
        priceTv.text = priceText
        ImagesHelper().getImage(url, cardImage,false)
    }





}